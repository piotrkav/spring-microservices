package com.isep.psidi.orderservice.domain.customer;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Customer {

    public Customer() {
    }

    public Customer(String customerName, String address) {
        this.id = ObjectId.get().toHexString();
        this.customerName = customerName;
        this.address = address;
        this.createdDate = new Date();
        this.hasOpenOrder = false;
    }

    @Id
    private String id;
    private String address;
    private String customerName;
    private Date createdDate;
    private boolean hasOpenOrder;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isHasOpenOrder() {
        return hasOpenOrder;
    }

    public void setHasOpenOrder(boolean hasOpenOrder) {
        this.hasOpenOrder = hasOpenOrder;
    }
}
