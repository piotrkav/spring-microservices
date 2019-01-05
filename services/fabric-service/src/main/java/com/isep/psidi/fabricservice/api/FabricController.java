package com.isep.psidi.fabricservice.api;

import com.isep.psidi.fabricservice.domain.fabric.AddFabricBodyRequest;
import com.isep.psidi.fabricservice.domain.fabric.Fabric;
import com.isep.psidi.fabricservice.domain.fabric.FabricTempItem;
import com.isep.psidi.fabricservice.domain.yarn.YarnItem;
import com.isep.psidi.fabricservice.service.ColorService;
import com.isep.psidi.fabricservice.service.FabricService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Path("fabric")
public class FabricController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private ColorService colorService;
    @Autowired
    private YarnTypeService yarnTypeService;
    @Autowired
    private YarnItemService yarnItemService;
    @Autowired
    private FabricService fabricService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFabrics() {
        List<Fabric> allFabricList = fabricService.getAllFabrics();
        return Response.ok(allFabricList).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFabric(@Valid @NotNull AddFabricBodyRequest bodyRequest) {

        if (checkIfFabricAlreadyExists(bodyRequest.getFabricCode())) {
            //TODO check if color is the same too
            return Response.status(Response.Status.CONFLICT).entity("Fabric " + bodyRequest.getFabricCode() + " already exists!").build();
        }

        if (!colorService.checkIfColorExists(bodyRequest.getColor())) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create fabric "
                    + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the color: " + bodyRequest.getColor() + " does not exists!").build();
        }

        List<FabricTempItem> tempItems = getTempItemsFromFabricCode(bodyRequest.getFabricCode());
        if (tempItems.size() == 0 || tempItems.size() > 4) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create fabric "
                    + bodyRequest.getFabricCode() + " " + bodyRequest.getColor()
                    + " because of the wrong number of yarn types; minimum number is 0; maximum is 4; given: " + tempItems.size()).build();


        }
        int total = tempItems.stream().mapToInt(FabricTempItem::getPercentage).sum();
        if (total != 100) {
            return Response.status(Response.Status.CONFLICT).entity("Cannot create fabric "
                    + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the yarn items does not sum to 100%").build();

        }

        for (FabricTempItem t : tempItems) {
            if (!yarnTypeService.checkIfYarnTypeExists(t.getCode())) {
                return Response.status(Response.Status.CONFLICT).entity("Cannot create fabric "
                        + bodyRequest.getFabricCode() + " " + bodyRequest.getColor() + " because the yarn: " + t.getCode() + " does not exists!").build();
            }
            if (!yarnItemService.checkIfYarnTypeExists(t.getPercentage(), t.getCode())) {
                YarnItem yarnItem = new YarnItem();
                yarnItem.setPercentage(t.getPercentage());
                yarnItem.setId(ObjectId.get().toHexString());
                yarnItem.setYarnType(yarnTypeService.getYarnTypeByCode(t.getCode()));
                yarnItemService.createYarnItem(yarnItem);
            }
        }

        Fabric fabric = new Fabric();
        ObjectId objectId = new ObjectId();
        fabric.setFabricCode(bodyRequest.getFabricCode());
        fabric.setId(objectId.toHexString());
        fabric.setColor(colorService.getColorByName(bodyRequest.getColor()));
        List<YarnItem> itemsForFabric = new ArrayList<>();
        tempItems.forEach(fabricTempItem -> itemsForFabric
                .add(yarnItemService.getYarnItemByPercentageAndCode(
                        fabricTempItem.getPercentage(), fabricTempItem.getCode())));
        fabric.setYarnItems(itemsForFabric);
        fabricService.createFabric(fabric);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(fabric.getId()).build()).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFabricById(@PathParam("id") String id) {
        Fabric fabric = fabricService.getFabricById(id);
        if (fabric != null) {
            return Response.ok(fabric).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/code/{code : .+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFabricByCode(@PathParam("code") String code) {

        Fabric fabric = fabricService.getFabricByFabricCode(code);
        if (fabric != null) {
            return Response.ok(fabric).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private boolean checkIfFabricAlreadyExists(String fabricCode) {
        return fabricService.checkIfYarnTypeExists(fabricCode);
    }

    List<FabricTempItem> getTempItemsFromFabricCode(String fabricCode) {
        List<String> yarnTypesCodes = new ArrayList<>(Arrays.asList(fabricCode.split(",")));
        List<FabricTempItem> items = new ArrayList<>();
        for (String code : yarnTypesCodes) {
            String[] part = code.split("(?<=\\D)(?=\\d)");
            items.add(new FabricTempItem(Integer.parseInt(part[1]), part[0]));
        }
        return items;
    }


}
