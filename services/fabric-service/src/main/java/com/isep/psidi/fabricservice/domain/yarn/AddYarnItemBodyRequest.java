package com.isep.psidi.fabricservice.domain.yarn;

public class AddYarnItemBodyRequest {

    private int percentage;
    private String code;

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
