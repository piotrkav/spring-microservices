package com.isep.psidi.orderservice.repository;

import com.isep.psidi.orderservice.domain.order.OrderLine;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderLineRepository extends MongoRepository<OrderLine, String> {
    public List<OrderLine> findByOrderId(String orderId);
}
