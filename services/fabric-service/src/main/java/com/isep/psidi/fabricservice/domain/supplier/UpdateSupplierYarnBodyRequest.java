package com.isep.psidi.fabricservice.domain.supplier;

import java.util.Date;

public class UpdateSupplierYarnBodyRequest {
    private double quantity;
    private double pricePerKG;
    private Date validityDate;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPricePerKG() {
        return pricePerKG;
    }

    public void setPricePerKG(double pricePerKG) {
        this.pricePerKG = pricePerKG;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }
}
