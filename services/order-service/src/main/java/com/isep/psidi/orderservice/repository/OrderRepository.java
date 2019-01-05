package com.isep.psidi.orderservice.repository;

import com.isep.psidi.orderservice.domain.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findByCustomerId(String customerId);

    List<Order> findBySupplierId(String supplierId);

}
