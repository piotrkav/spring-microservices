package com.isep.psidi.fabricservice.api;

import com.isep.psidi.fabricservice.domain.yarn.AddYarnItemBodyRequest;
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
@Path("yarn-item")
public class YarnItemController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private YarnTypeService yarnTypeService;


    @Autowired
    private YarnItemService yarnItemService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYarnItems() {
        List<YarnItem> allYarnItems = yarnItemService.getAllYarnItems();
        return Response.ok(allYarnItems).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createYarnItem(@Valid @NotNull AddYarnItemBodyRequest bodyRequest) {
        if (checkIfYarnItemAlreadyExists(bodyRequest.getPercentage(), bodyRequest.getCode())) {
            return Response.status(Response.Status.CONFLICT).entity("Yarn item " + bodyRequest.getPercentage() + " " + bodyRequest.getCode() + " already exists!").build();
        }

        if (!yarnTypeService.checkIfYarnTypeExists(bodyRequest.getCode())) {
            return Response.status(Response.Status.NOT_FOUND).entity("Cannot create YarnItem "
                    + bodyRequest.getPercentage()
                    + " " + bodyRequest.getCode()
                    + " because YarnType "
                    + bodyRequest.getCode()
                    + "was not found").build();
        }

        ObjectId objectId = new ObjectId();
        YarnType yarnType = yarnTypeService.getYarnTypeByCode(bodyRequest.getCode());
        YarnItem yarnItem = new YarnItem();
        yarnItem.setId(objectId.toHexString());
        yarnItem.setPercentage(bodyRequest.getPercentage());
        yarnItem.setYarnType(yarnType);
        yarnItemService.createYarnItem(yarnItem);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(yarnItem.getId()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getYarnItemById(@PathParam("id") String id) {
        YarnItem yarnItem = yarnItemService.getYarnItemById(id);
        if (yarnItem != null) {
            return Response.ok(yarnItem).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public boolean checkIfYarnItemAlreadyExists(int percentage, String code) {
        return yarnItemService.checkIfYarnTypeExists(percentage, code);
    }


}