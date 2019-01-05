package com.isep.psidi.fabricservice.domain.supplier;

import java.util.Date;

public class AddSupplierYarnBodyRequest {

    private String supplierId;
    private String yarnCode;
    private double quantity;
    private double pricePerKG;
    private Date validityDate;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getYarnCode() {
        return yarnCode;
    }

    public void setYarnCode(String yarnCode) {
        this.yarnCode = yarnCode;
    }

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
