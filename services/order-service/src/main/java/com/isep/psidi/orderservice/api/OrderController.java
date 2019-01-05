package com.isep.psidi.orderservice.api;


import com.isep.psidi.orderservice.domain.fabric.AddFabricBodyRequest;
import com.isep.psidi.orderservice.domain.fabric.Fabric;
import com.isep.psidi.orderservice.domain.order.AddOrderBodyRequest;
import com.isep.psidi.orderservice.domain.order.Order;
import com.isep.psidi.orderservice.domain.order.OrderLine;
import com.isep.psidi.orderservice.domain.order.OrderStatus;
import com.isep.psidi.orderservice.domain.pricing.PricingHelper;
import com.isep.psidi.orderservice.domain.supplier.Supplier;
import com.isep.psidi.orderservice.domain.yarn.YarnDetail;
import com.isep.psidi.orderservice.domain.yarn.YarnItem;
import com.isep.psidi.orderservice.service.CustomerService;
import com.isep.psidi.orderservice.service.OrderService;
import com.isep.psidi.orderservice.service.OutputChannel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Component
@Path("orders")
public class OrderController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;


    @Autowired
    private FabricApiClient fabricApiClient;

    @Autowired
    @Qualifier(OutputChannel.YARN_NEEDED_OUTPUT)
    private MessageChannel yarnNeededMessageChannel;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();
        return Response.ok(allOrders).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(@Valid @NotNull AddOrderBodyRequest bodyRequest) {
        if (!customerService.checkIfCustomerExists(bodyRequest.getCustomerId())) {
            return Response.status(Response.Status.CONFLICT).entity("Customer " + bodyRequest.getCustomerId() + " does not exist!").build();
        }
        if (customerService.checkIfCustomerHasOpenOrder(bodyRequest.getCustomerId())) {
            return Response.status(Response.Status.CONFLICT).entity("Customer " + bodyRequest.getCustomerId() + " has already ongoing order!").build();

        }
        if (!fabricApiClient.checkIfColorExists(bodyRequest.getColor())) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create Order with"
                    + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the color: " + bodyRequest.getColor() + " does not exists!").build();
        }
        //Fabric
        Fabric fabric = fabricApiClient.getFabric(bodyRequest.getFabricCode());
        if (fabric == null) {
            AddFabricBodyRequest addFabricBodyRequest = new AddFabricBodyRequest();
            addFabricBodyRequest.setColor(bodyRequest.getColor());
            addFabricBodyRequest.setFabricCode(bodyRequest.getFabricCode());
            boolean success = fabricApiClient.createFabric(addFabricBodyRequest);
            if (!success) {
                return Response.status(Response.Status.CONFLICT).entity("Cannot create Order with"
                        + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the fabric could not be created").build();
            } else {
                fabric = fabricApiClient.getFabric(bodyRequest.getFabricCode());
            }
        }
        //ORDER
        Order order = new Order();
        order.setId(new ObjectId().toHexString());
        order.setCustomerId(bodyRequest.getCustomerId());
        order.setFabric(fabric);
        order.setCreatedDate(new Date());
        order.setQuantity(bodyRequest.getQuantity());
        order.setMaterialPrice(0);
        order.setTotalPrice(0);
        order.setDyed(bodyRequest.isDyed());
        order.setStamped(bodyRequest.isStamped());

        //ORDERLINES
        List<OrderLine> orderLines = new ArrayList<>();
        for (YarnItem fabricYarnItem : fabric.getYarnItems()) {
            Optional<YarnDetail> details = bodyRequest.getYarnDetails().stream().filter(yarnDetail -> yarnDetail.getCodePercentage()
                    .equals(fabricYarnItem.getYarnType().getCode() + fabricYarnItem.getPercentage())).findAny();
            if (!details.isPresent()) {
                return Response.status(Response.Status.CONFLICT).entity("Cannot create Order with"
                        + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the Yarn details could not be found").build();
            }
            OrderLine orderLine = new OrderLine();
            orderLine.setId(new ObjectId().toHexString());
            orderLine.setFabricId(order.getFabric().getId());
            orderLine.setNETitle(details.get().getNETitle());
            orderLine.setNumberOfCables(details.get().getNumberOfCables());
            orderLine.setNumberOfFilaments(details.get().getNumberOfFilaments());
            orderLine.setQuantity(fabricYarnItem.getPercentage() * order.getQuantity() / 100);
            orderLine.setTMP(details.get().getTMP());
            orderLine.setYarnItem(fabricYarnItem);
            orderLine.setOrderId(order.getId());
            orderService.createOrderLine(orderLine);
            orderLines.add(orderLine);
        }
        order.setOrderLines(orderLines);

        //SUPPLIER
        Supplier supplier = fabricApiClient.getRandomSupplier();
        if (supplier == null) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create Order with"
                    + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + "because any supplier cannot be found").build();
        }
        order.setSupplierId(supplier.getId());
        boolean canProceed = fabricApiClient.checkIfSupplierCanProcessFabric(fabric, supplier.getId(), bodyRequest.getQuantity());
        if (canProceed) {
            fabricApiClient.updateSupplierYarns(order.getFabric(), order.getSupplierId(), order.getQuantity());
            order.setOrderStatus(OrderStatus.PROCESSING_KNITTING);
            fabric.getYarnItems().forEach(fabricYarnItem -> {
                double priceOfYarnPerKG = fabricApiClient.getSupplierYarnPricePerKG(fabricYarnItem.getYarnType().getId(), supplier.getId());
                double priceOfYarn = (((double) fabricYarnItem.getPercentage()) / 100) * order.getQuantity() * priceOfYarnPerKG;
                order.setMaterialPrice(order.getMaterialPrice() + priceOfYarn);
            });
            order.setTotalPrice(order.getTotalPrice() + order.getMaterialPrice());

        } else {
            yarnNeededMessageChannel.send(MessageBuilder.withPayload("Order id: "
                    + order.getId() + "requires more yarns than supplier: "
                    + order.getSupplierId() + " possess").build());

            order.setOrderStatus(OrderStatus.PENDING);
        }
        order.setCurrentProcessingStartDate(new Date());
        orderService.createOrder(order);
        customerService.setCustomerIsOpenOrder(order.getCustomerId(), true);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(order.getId()).build()).build();

    }

    @PUT
    @Path("changeStatus/{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeStatus(@PathParam("orderId") String orderId) {
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cannot update order because order with id: " + orderId + "does not exists").build();
        }
        switch (order.getOrderStatus()) {
            case PENDING:
                boolean canProceedFromPending = fabricApiClient.checkIfSupplierCanProcessFabric(order.getFabric(), order.getSupplierId(), order.getQuantity());
                if (canProceedFromPending) {
                    //TODO check if working
                    fabricApiClient.updateSupplierYarns(order.getFabric(), order.getSupplierId(), order.getQuantity());
                    order.setOrderStatus(OrderStatus.PROCESSING_KNITTING);
                    order.getFabric().getYarnItems().forEach(fabricYarnItem -> {
                        double priceOfYarnPerKG = fabricApiClient.getSupplierYarnPricePerKG(fabricYarnItem.getYarnType().getId(), order.getSupplierId());
                        double priceOfYarn = (((double) fabricYarnItem.getPercentage()) / 100) * order.getQuantity() * priceOfYarnPerKG;
                        order.setMaterialPrice(order.getMaterialPrice() + priceOfYarn);
                    });
                    order.setTotalPrice(order.getTotalPrice() + order.getMaterialPrice());
                    order.setCurrentProcessingStartDate(new Date());
                    return buildResponseForUpdate(OrderStatus.PROCESSING_KNITTING, OrderStatus.PROCESSING_PRETREATMENT, order);
                }
                return Response.ok().entity("Cannot proceed the order: " + order.getId()
                        + " from " + OrderStatus.PENDING.toString() + " to " + OrderStatus.PROCESSING_KNITTING.toString()
                        + " because the supplier: " + order.getSupplierId()
                        + " has no sufficient yarns").build();
            case PROCESSING_KNITTING:
                order.setKnittingPrice(PricingHelper.KNITTING_PRICE_PER_KG * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getKnittingPrice());
                order.setCurrentProcessingStartDate(new Date());
                return buildResponseForUpdate(OrderStatus.PROCESSING_KNITTING, OrderStatus.PROCESSING_PRETREATMENT, order);
            case PROCESSING_PRETREATMENT:
                order.setPretreatmentPrice(PricingHelper.PRETREATMENT_PRICE_PER_KG * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getPretreatmentPrice());
                order.setCurrentProcessingStartDate(new Date());
                if (order.isDyed()) {
                    return buildResponseForUpdate(OrderStatus.PROCESSING_PRETREATMENT, OrderStatus.PROCESSING_DYEING, order);
                } else if (order.isStamped()) {
                    order.setDyeingPrice(0);
                    return buildResponseForUpdate(OrderStatus.PROCESSING_PRETREATMENT, OrderStatus.PROCESSING_STAMPING, order);
                } else {
                    order.setDyeingPrice(0);
                    order.setStampingPrice(0);
                    return buildResponseForUpdate(OrderStatus.PROCESSING_PRETREATMENT, OrderStatus.PROCESSING_FINISHING, order);
                }
            case PROCESSING_DYEING:
                order.setDyeingPrice(PricingHelper.getColorPriceOfDyeing(order.getFabric().getColor().getColorType()) * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getDyeingPrice());
                order.setCurrentProcessingStartDate(new Date());
                if (order.isStamped()) {
                    return buildResponseForUpdate(OrderStatus.PROCESSING_DYEING, OrderStatus.PROCESSING_STAMPING, order);
                } else {
                    order.setStampingPrice(0);
                    return buildResponseForUpdate(OrderStatus.PROCESSING_DYEING, OrderStatus.PROCESSING_FINISHING, order);
                }
            case PROCESSING_STAMPING:
                order.setStampingPrice(PricingHelper.STAMPING_PRICE * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getStampingPrice());
                order.setCurrentProcessingStartDate(new Date());
                return buildResponseForUpdate(OrderStatus.PROCESSING_STAMPING, OrderStatus.PROCESSING_FINISHING, order);
            case PROCESSING_FINISHING:
                order.setFinishingPrice(PricingHelper.FINISHING_PRICE * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getFinishingPrice());
                order.setCurrentProcessingStartDate(new Date());
                return buildResponseForUpdate(OrderStatus.PROCESSING_FINISHING, OrderStatus.PROCESSING_INSPECTION, order);
            case PROCESSING_INSPECTION:
                order.setInspectionPrice(PricingHelper.INSPECTION_PRICE * order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getInspectionPrice());
                order.setCurrentProcessingStartDate(new Date());
                return buildResponseForUpdate(OrderStatus.PROCESSING_INSPECTION, OrderStatus.PROCESSING_PACKING, order);
            case PROCESSING_PACKING:
                order.setPackingPrice(PricingHelper.PACKING_PRICE + order.getQuantity());
                order.setTotalPrice(order.getTotalPrice() + order.getPackingPrice());
                order.setCurrentProcessingStartDate(new Date());
                customerService.setCustomerIsOpenOrder(order.getCustomerId(), false);
                return buildResponseForUpdate(OrderStatus.PROCESSING_PACKING, OrderStatus.FINISHED, order);
            case FINISHED:
                return Response.status(Response.Status.CONFLICT).entity("Cannot update finished order!").build();
        }
        return Response.ok().build();
    }

    private Response buildResponseForUpdate(OrderStatus from, OrderStatus to, Order order) {
        order.setOrderStatus(to);
        orderService.updateOrder(order);
        return Response.ok().entity("Order: " + order.getId()
                + " was changed from status: " + from.toString()
                + " to " + to.toString()).build();
    }

    @GET
    @Path("{orderId}/details")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderLinesByOrderId(@PathParam("orderId") String orderid) {
        List<OrderLine> orderLines = orderService.getOrderLinesByOrderId(orderid);
        return Response.ok(orderLines).build();
    }

    @GET
    @Path("customer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("id") String id) {
        List<Order> customerOrders = customerService.getCustomerOrders(id);
        return Response.ok(customerOrders).build();
    }

    @GET
    @Path("supplier/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplierOrders(@PathParam("id") String id) {
        List<Order> supplierOrders = customerService.getSupplierOrders(id);
        return Response.ok(supplierOrders).build();
    }

    @GET
    @Path("{orderId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderByOrderId(@PathParam("orderId") String id) {
        Order order = orderService.getOrderById(id);
        return Response.ok(order).build();
    }

    @GET
    @Path("download/{orderId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadOrder(@PathParam("orderId") String id) {
        Order order = orderService.getOrderById(id);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(order);
        String fileName = "order_" + order.getId() + ".json";

        StreamingOutput fileStream = output -> {
            try {

                byte[] data = prettyJson.getBytes();
                output.write(data);
                output.flush();
            } catch (Exception e) {
                Response.status(404).entity("Error reading bytes from order");
            }
        };
        return Response
                .ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition", "attachment; filename = " + fileName)
                .build();
    }

    @GET
    @Path("top10")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTop10Orders() {
        List<Order> orders = orderService.getAllOrders();
        orders.sort(Comparator.comparingDouble(Order::getTotalPrice).reversed());
        return Response.ok(orders.stream().limit(10)).build();
    }
}
