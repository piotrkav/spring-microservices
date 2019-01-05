package com.isep.psidi.fabricservice.domain.supplier;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Supplier {

    @Id
    private String id;
    private String supplierName;
    private String contactPerson;
    private String address;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
