package com.isep.psidi.fabricservice;

import com.isep.psidi.fabricservice.service.InputChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({InputChannel.class})
public class FabricServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FabricServiceApplication.class, args);
    }

}

