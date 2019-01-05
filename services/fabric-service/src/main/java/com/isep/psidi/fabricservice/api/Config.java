package com.isep.psidi.fabricservice.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api/psidi")
public class Config extends ResourceConfig {

    public Config() {
        registerResources();
    }

    private void registerResources() {
        register(ColorController.class);
        register(YarnTypeController.class);
        register(YarnItemController.class);
        register(FabricController.class);
        register(SupplierYarnController.class);
        register(SupplierController.class);
    }
}
