package com.isep.psidi.orderservice.domain.fabric;

public class AddFabricBodyRequest {

    private String fabricCode;
    private String color;

    public String getFabricCode() {
        return fabricCode;
    }

    public void setFabricCode(String fabricCode) {
        this.fabricCode = fabricCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
