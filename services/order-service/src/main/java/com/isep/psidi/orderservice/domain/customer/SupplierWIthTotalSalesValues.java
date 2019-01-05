package com.isep.psidi.orderservice.domain.customer;

import com.isep.psidi.orderservice.domain.supplier.Supplier;

public class SupplierWIthTotalSalesValues {

    private final Supplier supplier;
    private final double totalSalesValue;

    public SupplierWIthTotalSalesValues(Supplier supplier, double totalSalesValue) {
        this.supplier = supplier;
        this.totalSalesValue = totalSalesValue;
    }

    public double getTotalSalesValue() {
        return totalSalesValue;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
