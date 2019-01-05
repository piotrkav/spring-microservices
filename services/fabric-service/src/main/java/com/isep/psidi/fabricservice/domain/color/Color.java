package com.isep.psidi.fabricservice.domain.color;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Color {

    @Id
    private String id;

    private String name;

    private ColorType colorType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }
}
