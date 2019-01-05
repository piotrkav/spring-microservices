package com.isep.psidi.fabricservice.api;


import com.isep.psidi.fabricservice.domain.supplier.AddSupplierYarnBodyRequest;
import com.isep.psidi.fabricservice.domain.supplier.SupplierYarn;
import com.isep.psidi.fabricservice.domain.supplier.UpdateSupplierYarnBodyRequest;
import com.isep.psidi.fabricservice.service.SupplierService;
import com.isep.psidi.fabricservice.service.SupplierYarnService;
import com.isep.psidi.fabricservice.service.YarnItemService;
import com.isep.psidi.fabricservice.service.YarnTypeService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Date;
import java.util.List;

@Component
@Path("supplier-item")
public class SupplierYarnController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private YarnTypeService yarnTypeService;

    @Autowired
    private YarnItemService yarnItemService;

    @Autowired
    private SupplierYarnService supplierYarnService;

    @Autowired
    private SupplierService supplierService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSupplierYarns() {
        List<SupplierYarn> allSupplierYarns = supplierYarnService.getAllSupplierYarns();
        return Response.ok(allSupplierYarns).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSupplierYarn(@Valid @NotNull AddSupplierYarnBodyRequest bodyRequest) {

        if (!yarnTypeService.checkIfYarnTypeExists(bodyRequest.getYarnCode())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cannot create SupplierYarn "
                    + bodyRequest.getYarnCode()
                    + " because YarnType "
                    + bodyRequest.getYarnCode()
                    + "was not found").build();

        }
        if (!supplierService.checkIfSupplierExists(bodyRequest.getSupplierId())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cannot create SupplierYarn "
                    + bodyRequest.getYarnCode()
                    + " because Supplier with id "
                    + bodyRequest.getSupplierId()
                    + "was not found").build();
        }
        if (supplierYarnService.checkIfSupplierYarnExists(bodyRequest.getSupplierId(), bodyRequest.getYarnCode())) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create SupplierYarn "
                    + bodyRequest.getYarnCode() + " for supplier " + bodyRequest.getSupplierId()
                    + " because it already exists!").build();
        }

        ObjectId objectId = new ObjectId();
        SupplierYarn supplierYarn = new SupplierYarn();
        supplierYarn.setId(objectId.toHexString());
        supplierYarn.setYarnType(yarnTypeService.getYarnTypeByCode(bodyRequest.getYarnCode()));
        supplierYarn.setQuantity(bodyRequest.getQuantity());
        supplierYarn.setPricePerKG(bodyRequest.getPricePerKG());
        supplierYarn.setPriceDate(new Date());
        supplierYarn.setValidityPrice(bodyRequest.getValidityDate());
        supplierYarn.setSupplierId(bodyRequest.getSupplierId());
        supplierYarnService.createSupplierYarn(supplierYarn);


//        Supplier supplier = supplierService.getSupplierById(bodyRequest.getSupplierId());
//        List<SupplierYarn> currentSupplierList = (supplier.getSupplierYarns() == null ? new ArrayList<>() : supplier.getSupplierYarns());
//        currentSupplierList.add(supplierYarn);
//        supplier.setSupplierYarns(currentSupplierList);
//        supplierService.updateSupplier(supplier);

        return Response.created(uriInfo.getAbsolutePathBuilder().path(supplierYarn.getId()).build()).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSupplierYarn(@PathParam("id") String id, @Valid @NotNull UpdateSupplierYarnBodyRequest bodyRequest) {
        SupplierYarn supplierYarn = supplierYarnService.getSupplierYarnById(id);
        supplierYarn.setValidityPrice(bodyRequest.getValidityDate());
        supplierYarn.setPriceDate(new Date());
        supplierYarn.setQuantity(bodyRequest.getQuantity());
        supplierYarn.setPricePerKG(bodyRequest.getPricePerKG());
        supplierYarnService.updateSupplierYarn(supplierYarn);
        return Response.ok().entity("Supplier Yarn " + id + " updated").build();
    }

    @GET
    @Path("{supplierId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplierYarnsBySupplier(@PathParam("supplierId") String supplierId) {
        List<SupplierYarn> allSupplierYarns = supplierYarnService.getSupplierYarnBySupplierId(supplierId);
        return Response.ok(allSupplierYarns).build();
    }

    @GET
    @Path("/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplierYarnById(@PathParam("id") String id) {
        SupplierYarn supplierYarn = supplierYarnService.getSupplierYarnById(id);
        return Response.ok(supplierYarn).build();
    }


}
