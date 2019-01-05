package com.isep.psidi.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Spring Boot application entry point.
 *
 * @author cassiomolin
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServiceDiscoveryApplication.class, args);
    }
}