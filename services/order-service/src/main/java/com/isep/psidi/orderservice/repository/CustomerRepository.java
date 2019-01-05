package com.isep.psidi.orderservice.repository;

import com.isep.psidi.orderservice.domain.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    public Customer findByCustomerName(String customerName);
    public Customer findByCustomerNameAndAddress(String customerName, String address);
}
