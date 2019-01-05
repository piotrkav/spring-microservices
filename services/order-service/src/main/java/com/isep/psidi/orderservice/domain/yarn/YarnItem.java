package com.isep.psidi.orderservice.domain.yarn;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class YarnItem {

    public YarnItem() {
    }

    @Id
    private String id;
    private YarnType yarnType;
    private int percentage;

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }


    public YarnType getYarnType() {
        return yarnType;
    }

    public void setYarnType(YarnType yarnType) {
        this.yarnType = yarnType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
