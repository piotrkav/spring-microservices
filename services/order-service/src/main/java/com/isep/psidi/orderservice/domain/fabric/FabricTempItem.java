package com.isep.psidi.orderservice.domain.fabric;

public class FabricTempItem {

    private int percentage;
    private String code;

    public FabricTempItem(int p, String c) {
        this.percentage = p;
        this.code = c;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
