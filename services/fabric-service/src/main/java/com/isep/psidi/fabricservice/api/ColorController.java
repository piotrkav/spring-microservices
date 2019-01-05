package com.isep.psidi.fabricservice.api;

import com.isep.psidi.fabricservice.domain.color.AddColorBodyRequest;
import com.isep.psidi.fabricservice.domain.color.Color;
import com.isep.psidi.fabricservice.service.ColorService;
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
@Path("color")
public class ColorController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private ColorService colorService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColors() {
        List<Color> colors = colorService.getAllColors();
        return Response.ok(colors).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createColor(@Valid @NotNull AddColorBodyRequest bodyRequest) {
        if (checkIfColorExists(bodyRequest.getName())) {
            return Response.status(Response.Status.CONFLICT).entity("color " + bodyRequest.getName() + " already exists!").build();
        }
        ObjectId objectId = new ObjectId();
        Color color = new Color();
        color.setId(objectId.toHexString());
        color.setName(bodyRequest.getName());
        color.setColorType(bodyRequest.getColorType());
        colorService.createColor(color);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(objectId.toHexString()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColorById(@PathParam("id") String id) {
        Color color = colorService.getColorById(id);
        if (color != null) {
            return Response.ok(color).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/name/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColorByName(@PathParam("name") String name) {
        Color color = colorService.getColorByName(name);
        if (color != null) {
            return Response.ok(color).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public boolean checkIfColorExists(String name) {
        return colorService.checkIfColorExists(name);
    }


}
