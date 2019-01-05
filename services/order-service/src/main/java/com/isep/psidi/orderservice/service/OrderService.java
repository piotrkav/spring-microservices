package com.isep.psidi.orderservice.service;


import com.isep.psidi.orderservice.domain.order.Order;
import com.isep.psidi.orderservice.domain.order.OrderLine;
import com.isep.psidi.orderservice.repository.CustomerRepository;
import com.isep.psidi.orderservice.repository.OrderLineRepository;
import com.isep.psidi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findOne(id);
    }

    public List<OrderLine> getOrderLinesByOrderId(String orderId) {
        return orderLineRepository.findByOrderId(orderId);
    }

    public OrderLine getOrderLineById(String orderLineId) {
        return orderLineRepository.findOne(orderLineId);
    }

    public String createOrder(Order order) {
        orderRepository.save(order);
        return order.getId();
    }

    public String createOrderLine(OrderLine orderLine) {
        orderLineRepository.save(orderLine);
        return orderLine.getId();
    }

    public List<Order> getCustomerOrders(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getSupplierOrders(String supplierId) {
        return orderRepository.findBySupplierId(supplierId);
    }

    public void updateOrder(Order order) {
        order = orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        Order order = orderRepository.findOne(id);
        if (order == null) {
            throw new NotFoundException();
        } else {
            orderRepository.delete(order);
        }
    }

    public boolean checkIfOrderExists(String customerId) {
        return customerRepository.findOne(customerId) != null;
    }

    public boolean checkIfCustomerExistsByName(String name) {
        return customerRepository.findByCustomerName(name) != null;
    }


}
