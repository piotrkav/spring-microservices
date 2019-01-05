package com.isep.psidi.orderservice.api;

import com.isep.psidi.orderservice.domain.color.Color;
import com.isep.psidi.orderservice.domain.fabric.AddFabricBodyRequest;
import com.isep.psidi.orderservice.domain.fabric.Fabric;
import com.isep.psidi.orderservice.domain.supplier.Supplier;
import com.isep.psidi.orderservice.domain.supplier.SupplierYarn;
import com.isep.psidi.orderservice.domain.supplier.UpdateSupplierYarnBodyRequest;
import com.isep.psidi.orderservice.domain.yarn.YarnItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class FabricApiClient {

    private static final String FABRIC_SERVICE = "fabric-service";
    private static final String MAIN_URI_PART = "api/psidi";
    private Client client;
    @Autowired
    private LoadBalancerClient loadBalancer;

    @PostConstruct
    private void init() {
        this.client = ClientBuilder.newClient();
    }

    private URI getServiceURI() {

        ServiceInstance serviceInstance = loadBalancer.choose(FABRIC_SERVICE);
        if (serviceInstance == null) {
            throw new ServiceUnavailableException("Service unavailable");
        }
        return serviceInstance.getUri();
    }


    public Color getColor(String colorCode) {
        URI productServiceUri = getServiceURI();
        Response response = client.target(productServiceUri).path(MAIN_URI_PART).path("color/name").path(colorCode).request().get();
        System.out.println("LOCATION OF CALL: " + response.getLocation());
        if (Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily()) {
            return response.readEntity(Color.class);
        } else {
            return null;
        }
    }


    public boolean checkIfColorExists(String colorCode) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("color/name").path(colorCode).request().head();
        return Response.Status.OK.getStatusCode() == response.getStatus();
    }

    public Fabric getFabric(String fabricCode) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("fabric/code").path(fabricCode).request().get();
        if (Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily()) {
            return response.readEntity(Fabric.class);
        } else {
            return null;
        }
    }

    public boolean createFabric(AddFabricBodyRequest bodyRequest) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("fabric").request().post(Entity.json(bodyRequest));
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return true;
        } else {
            return false;
        }
    }

    public Supplier getRandomSupplier() {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier").request().get();
        if (Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily()) {
            List<Supplier> suppliers = response.readEntity(new GenericType<List<Supplier>>() {
            });
            Random rand = new Random();
            return suppliers.get(rand.nextInt(suppliers.size()));

        } else {
            return null;
        }
    }

    public List<Supplier> getAllSuppliers() {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier").request().get();
        if (Response.Status.Family.SUCCESSFUL == response.getStatusInfo().getFamily()) {
            return response.readEntity(new GenericType<List<Supplier>>() {
            });
        } else {
            return null;
        }
    }

    public boolean checkIfSupplierCanProcessFabric(Fabric fabric, String supplierId, double quantity) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier").path(supplierId).path("/yarns").request().get();
        List<SupplierYarn> suppliersYarns = response.readEntity(new GenericType<List<SupplierYarn>>() {
        });
        return checkIfSupplierCanProcessFabric(fabric, quantity, suppliersYarns);
    }


    private boolean checkIfSupplierCanProcessFabric(Fabric fabric, double quantity, List<
            SupplierYarn> supplierYarns) {
        for (YarnItem yarnItemInFabric : fabric.getYarnItems()) {

            double value = (((double) yarnItemInFabric.getPercentage()) / 100.0) * quantity;
            if (!checkIfSupplierHasYarn(supplierYarns, yarnItemInFabric.getYarnType().getId(), value)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfSupplierHasYarn(List<SupplierYarn> list, String yarnTypeId, double neededQuantity) {
        return list.stream().anyMatch(supplierYarn -> supplierYarn.getYarnType().getId().equals(yarnTypeId) && supplierYarn.getQuantity() >= neededQuantity);
    }

    public double getSupplierYarnPricePerKG(String yarnTypeId, String supplierId) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier").path(supplierId).path("/yarns").request().get();
        List<SupplierYarn> supplierYarns = response.readEntity(new GenericType<List<SupplierYarn>>() {
        });
        Optional<SupplierYarn> yarnOfSupplier = supplierYarns.stream().filter(supplierYarn -> supplierYarn.getYarnType().getId().equals(yarnTypeId)).findAny();//.get().getPricePerKG();
        return yarnOfSupplier.map(SupplierYarn::getPricePerKG).orElse(0.0);
    }

    public boolean updateSupplierYarnItem(String yarnItemId, UpdateSupplierYarnBodyRequest bodyRequest) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier-item").path(yarnItemId).request().put(Entity.json(bodyRequest));
        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateSupplierYarns(Fabric fabric, String supplierId, double quantity) {
        List<SupplierYarn> supplierYarns = getSupplierYarnsById(supplierId);
        boolean success = true;
        for (YarnItem fabricYarnItem : fabric.getYarnItems()) {
            Optional<SupplierYarn> yarnOfSupplier = supplierYarns.stream()
                    .filter(supplierYarn -> supplierYarn.getYarnType().getId()
                            .equals(fabricYarnItem.getYarnType().getId())).findAny();

            if (yarnOfSupplier.isPresent()) {
                SupplierYarn supplierYarn = yarnOfSupplier.get();
                UpdateSupplierYarnBodyRequest bodyRequest = new UpdateSupplierYarnBodyRequest();
                bodyRequest.setPricePerKG(supplierYarn.getPricePerKG());
                bodyRequest.setQuantity(supplierYarn.getQuantity() - (((double) fabricYarnItem.getPercentage()) / 100.0) * quantity);
                bodyRequest.setValidityDate(supplierYarn.getValidityPrice());
                success = updateSupplierYarnItem(supplierYarn.getId(), bodyRequest);
            }

        }
        return success;

    }

    private List<SupplierYarn> getSupplierYarnsById(String supplierId) {
        Response response = client.target(getServiceURI()).path(MAIN_URI_PART).path("supplier").path(supplierId).path("/yarns").request().get();
        List<SupplierYarn> supplierYarns = response.readEntity(new GenericType<List<SupplierYarn>>() {
        });

        return supplierYarns;
    }

}
