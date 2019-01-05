package com.isep.psidi.orderservice.domain.order;

import com.isep.psidi.orderservice.domain.yarn.YarnDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddOrderBodyRequest {

    private String customerId;
    private double quantity;
    private String fabricCode;
    private List<YarnDetail> yarnDetails;
    private String color;
    @JsonProperty
    private boolean isDyed;
    @JsonProperty
    private boolean isStamped;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getFabricCode() {
        return fabricCode;
    }

    public void setFabricCode(String fabricCode) {
        this.fabricCode = fabricCode;
    }

    public List<YarnDetail> getYarnDetails() {
        return yarnDetails;
    }

    public void setYarnDetails(List<YarnDetail> yarnDetails) {
        this.yarnDetails = yarnDetails;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isDyed() {
        return isDyed;
    }

    public void setDyed(boolean dyed) {
        isDyed = dyed;
    }

    public boolean isStamped() {
        return isStamped;
    }

    public void setStamped(boolean stamped) {
        isStamped = stamped;
    }
}
