package com.isep.psidi.fabricservice.domain.supplier;

import com.isep.psidi.fabricservice.domain.yarn.YarnType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class SupplierYarn {

    @Id
    private String id;
    private String supplierId;
    private YarnType yarnType;
    private double quantity;
    private Date priceDate;
    private double pricePerKG;
    private Date validityPrice;


    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }


    public YarnType getYarnType() {
        return yarnType;
    }

    public void setYarnType(YarnType yarnType) {
        this.yarnType = yarnType;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    public double getPricePerKG() {
        return pricePerKG;
    }

    public void setPricePerKG(double pricePerKG) {
        this.pricePerKG = pricePerKG;
    }

    public Date getValidityPrice() {
        return validityPrice;
    }

    public void setValidityPrice(Date validityPrice) {
        this.validityPrice = validityPrice;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
