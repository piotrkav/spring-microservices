package com.isep.psidi.orderservice.api;

import com.isep.psidi.orderservice.domain.color.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Component
@Path("test")
public class TestController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private FabricApiClient fabricApiClient;

    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getColorById(@PathParam("code") String code) {
        Color color = fabricApiClient.getColor(code);
        if (color != null) {
            return Response.ok(color).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
