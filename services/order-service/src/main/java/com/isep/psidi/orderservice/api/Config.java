package com.isep.psidi.orderservice.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("api/psidi2")
public class Config extends ResourceConfig {

    public Config() {
        registerResources();
    }

    private void registerResources() {
        register(TestController.class);
        register(CustomerController.class);
        register(OrderController.class);
    }

}
