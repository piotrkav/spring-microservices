package com.isep.psidi.fabricservice.domain.fabric;

import com.isep.psidi.fabricservice.domain.color.Color;
import com.isep.psidi.fabricservice.domain.yarn.YarnItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Fabric {

    @Id
    private String id;

    private String fabricCode;

    private List<YarnItem> yarnItems;

    private Color color;

    public Fabric() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFabricCode() {
        return fabricCode;
    }

    public void setFabricCode(String fabricCode) {
        this.fabricCode = fabricCode;
    }

    public List<YarnItem> getYarnItems() {
        return yarnItems;
    }

    public void setYarnItems(List<YarnItem> yarnItems) {
        this.yarnItems = yarnItems;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
