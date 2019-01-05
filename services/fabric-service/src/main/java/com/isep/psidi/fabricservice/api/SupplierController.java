package com.isep.psidi.fabricservice.api;

import com.isep.psidi.fabricservice.domain.supplier.AddSupplierBodyRequest;
import com.isep.psidi.fabricservice.domain.supplier.Supplier;
import com.isep.psidi.fabricservice.domain.supplier.SupplierYarn;
import com.isep.psidi.fabricservice.service.SupplierService;
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
import java.util.List;

@Component
@Path("supplier")
public class SupplierController {

    @Context
    private UriInfo uriInfo;

//    @Autowired
//    private YarnTypeService yarnTypeService;
//
//    @Autowired
//    private YarnItemService yarnItemService;
//
//    @Autowired
//    private SupplierYarnService supplierYarnService;

    @Autowired
    private SupplierService supplierService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSuppliers() {
        List<Supplier> allSuppliers = supplierService.getAllSuppliers();
        return Response.ok(allSuppliers).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplierById(@PathParam("id") String id) {
        Supplier supplier = supplierService.getSupplierById(id);
        return Response.ok(supplier).build();
    }


    @GET
    @Path("{id}/yarns")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupplierYarns(@PathParam("id") String id) {
        List<SupplierYarn> supplierYarns = supplierService.getSupplierYarns(id);
        return Response.ok(supplierYarns).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSupplier(@Valid @NotNull AddSupplierBodyRequest bodyRequest) {
        if (supplierService.checkIfSupplierExistsByName(bodyRequest.getSupplierName())) {
            return Response.status(Response.Status.CONFLICT).entity("Supplier " + bodyRequest.getSupplierName() + " already exists!").build();
        }
        Supplier supplier = new Supplier();
        supplier.setAddress(bodyRequest.getAddress());
        supplier.setContactPerson(bodyRequest.getContactPerson());
        supplier.setSupplierName(bodyRequest.getSupplierName());
        supplier.setId(new ObjectId().toHexString());
        supplierService.createSupplier(supplier);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(supplier.getId()).build()).build();
    }


}

