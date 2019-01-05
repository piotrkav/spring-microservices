package com.isep.psidi.orderservice.domain.customer;

public class CustomerWithTotalSalesValues {

    private Customer customer;
    private double totalSalesValues;

    public CustomerWithTotalSalesValues(Customer customer, double totalSalesValues){
        this.customer = customer;
        this.totalSalesValues = totalSalesValues;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalSalesValues() {
        return totalSalesValues;
    }

    public void setTotalSalesValues(double totalSalesValues) {
        this.totalSalesValues = totalSalesValues;
    }
}
