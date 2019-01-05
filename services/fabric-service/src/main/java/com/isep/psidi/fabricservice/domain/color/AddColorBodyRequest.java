package com.isep.psidi.fabricservice.domain.color;

public class AddColorBodyRequest {

    private String name;
    private ColorType colorType;

    public AddColorBodyRequest() {
    }

    public String getName() {
        return name;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }

    public void setName(String name) {
        this.name = name;
    }
}
