package com.isep.psidi.fabricservice.api;

import com.isep.psidi.fabricservice.domain.yarn.AddYarnBodyRequest;
import com.isep.psidi.fabricservice.domain.yarn.YarnItem;
import com.isep.psidi.fabricservice.domain.yarn.YarnType;
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
import java.util.List;

@Component
@Path("yarn-type")
public class YarnTypeController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private YarnTypeService yarnTypeService;

    @Autowired
    private YarnItemService yarnItemService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYarnTypes() {
        List<YarnType> yarnTypeList = yarnTypeService.getAllYarnTypes();
        return Response.ok(yarnTypeList).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createYarnType(@Valid @NotNull AddYarnBodyRequest bodyRequest) {
        if (checkIfYarnTypeAlreadyExists(bodyRequest.getCode())) {
            return Response.status(Response.Status.CONFLICT).entity("Yarn type " + bodyRequest.getDescription() + " already exists!").build();
        }

        YarnType yarnType = new YarnType(bodyRequest.getCode(), bodyRequest.getDescription());
        yarnTypeService.createYarnType(yarnType);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(yarnType.getId()).build()).build();
    }
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createYarnTypeWithItems(@Valid @NotNull AddYarnBodyRequest bodyRequest) {
        if (checkIfYarnTypeAlreadyExists(bodyRequest.getCode())) {
            return Response.status(Response.Status.CONFLICT).entity("Yarn type " + bodyRequest.getDescription() + " already exists!").build();
        }
        YarnType yarnType = new YarnType(bodyRequest.getCode(), bodyRequest.getDescription());
        yarnTypeService.createYarnType(yarnType);

        for (int i = 1; i <= 100; i++) {
            YarnItem yarnItem = new YarnItem();
            String itemObjectId = new ObjectId().toHexString();
            yarnItem.setId(itemObjectId);
            yarnItem.setPercentage(i);
            yarnItem.setYarnType(yarnType);
            yarnItemService.createYarnItem(yarnItem);
        }
        //TODO replace the URI without /create
        return Response.created(uriInfo.getAbsolutePathBuilder().path(yarnType.getId()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYarnTypeById(@PathParam("id") String id) {
        YarnType yarnType = yarnTypeService.getYarnTypeById(id);
        if (yarnType != null) {
            return Response.ok(yarnType).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/code/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYarnTypeByCode(@PathParam("code") String code) {
        YarnType yarnType = yarnTypeService.getYarnTypeByCode(code);
        if (yarnType != null) {
            return Response.ok(yarnType).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public boolean checkIfYarnTypeAlreadyExists(String code) {
        return yarnTypeService.checkIfYarnTypeExists(code);
    }


}
