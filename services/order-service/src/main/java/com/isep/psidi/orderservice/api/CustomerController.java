package com.isep.psidi.orderservice.api;


import com.isep.psidi.orderservice.domain.customer.AddCustomerBodyRequest;
import com.isep.psidi.orderservice.domain.customer.Customer;
import com.isep.psidi.orderservice.domain.customer.CustomerWithTotalSalesValues;
import com.isep.psidi.orderservice.domain.customer.SupplierWIthTotalSalesValues;
import com.isep.psidi.orderservice.domain.order.Order;
import com.isep.psidi.orderservice.domain.supplier.Supplier;
import com.isep.psidi.orderservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Path("customer")
public class CustomerController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private FabricApiClient fabricApiClient;

    @Autowired
    private CustomerService customerService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        return Response.ok(allCustomers).build();
    }


    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerById(@PathParam("id") String id) {
        Customer customer = customerService.getCustomerById(id);
        return Response.ok(customer).build();
    }


    @GET
    @Path("{id}/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerOrders(@PathParam("id") String id) {
        List<Order> customerOrders = customerService.getCustomerOrders(id);
        return Response.ok(customerOrders).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") String id) {
        customerService.deleteCustomer(id);
        return Response.noContent().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCustomer(@Valid @NotNull AddCustomerBodyRequest bodyRequest) {

        if (customerService.checkIfCustomerExistsByNameAndAddress(bodyRequest.getCustomerName(), bodyRequest.getAddress())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Customer "
                            + bodyRequest.getCustomerName() + "living at: "
                            + bodyRequest.getAddress() + " already exists!").build();
        }
        Customer customer = new Customer(bodyRequest.getCustomerName(), bodyRequest.getAddress());
        customerService.createCustomer(customer);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(customer.getId()).build()).build();
    }

    @GET
    @Path("top10")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTop10Customers() {

        List<CustomerWithTotalSalesValues> customerWithTotalSalesValues = new ArrayList<>();
        List<Customer> allCustomers = customerService.getAllCustomers();
        for (Customer customer : allCustomers) {
            List<Order> customerOrders = customerService.getCustomerOrders(customer.getId());
            double customerOrdersTotalPrice = 0;
            if (customerOrders != null) {
                customerOrdersTotalPrice = customerOrders.stream().mapToDouble(Order::getTotalPrice).sum();
            }
            customerWithTotalSalesValues.add(new CustomerWithTotalSalesValues(customer, customerOrdersTotalPrice));
        }
        customerWithTotalSalesValues.sort(Comparator.comparingDouble(CustomerWithTotalSalesValues::getTotalSalesValues).reversed());
        return Response.ok(customerWithTotalSalesValues.stream().limit(10)).build();
    }

    @GET
    @Path("top10suppliers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTop10Suppliers() {

        List<SupplierWIthTotalSalesValues> supplierWIthTotalSalesValues = new ArrayList<>();
        List<Supplier> allSuppliers = fabricApiClient.getAllSuppliers();
        for (Supplier supplier : allSuppliers) {
            List<Order> ordersProvidedBySuppliers = customerService.getSupplierOrders(supplier.getId());
            double supplierOrdersTotalPrice = 0;
            if (ordersProvidedBySuppliers != null) {
                supplierOrdersTotalPrice = ordersProvidedBySuppliers.stream().mapToDouble(Order::getMaterialPrice).sum();
            }
            supplierWIthTotalSalesValues.add(new SupplierWIthTotalSalesValues(supplier, supplierOrdersTotalPrice));
        }
        supplierWIthTotalSalesValues.sort(Comparator.comparingDouble(SupplierWIthTotalSalesValues::getTotalSalesValue).reversed());
        return Response.ok(supplierWIthTotalSalesValues.stream().limit(10)).build();
    }
}

