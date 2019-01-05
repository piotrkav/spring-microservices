package com.isep.psidi.orderservice.service;

import com.isep.psidi.orderservice.domain.customer.Customer;
import com.isep.psidi.orderservice.domain.order.Order;
import com.isep.psidi.orderservice.repository.CustomerRepository;
import com.isep.psidi.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.util.List;

@Service
public class CustomerService {


    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderRepository orderRepository;


    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findOne(id);
    }

    public String createCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer.getId();
    }

    public List<Order> getCustomerOrders(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getSupplierOrders(String supplierId) {
        return orderRepository.findBySupplierId(supplierId);
    }

    public void updateCustomer(Customer customer) {
        customer = customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        Customer customer = customerRepository.findOne(id);
        if (customer == null) {
            throw new NotFoundException();
        } else {
            customerRepository.delete(customer);
        }
    }

    public void setCustomerIsOpenOrder(String id, boolean isOpenOrder) {
        Customer customer = getCustomerById(id);
        customer.setHasOpenOrder(isOpenOrder);
        updateCustomer(customer);
    }

    public boolean checkIfCustomerExists(String customerId) {
        return customerRepository.findOne(customerId) != null;
    }

    public boolean checkIfCustomerExistsByName(String name) {
        return customerRepository.findByCustomerName(name) != null;
    }

    public boolean checkIfCustomerExistsByNameAndAddress(String name, String address) {
        return customerRepository.findByCustomerNameAndAddress(name, address) != null;
    }


    public boolean checkIfCustomerHasOpenOrder(String customerId) {
        if (!checkIfCustomerExists(customerId)) {
            return false;
        }
        Customer customer = customerRepository.findOne(customerId);
        return  customer.isHasOpenOrder();
    }


}

