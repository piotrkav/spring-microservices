package com.isep.psidi.orderservice;

import com.isep.psidi.orderservice.service.OutputChannel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding({OutputChannel.class})
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

}

