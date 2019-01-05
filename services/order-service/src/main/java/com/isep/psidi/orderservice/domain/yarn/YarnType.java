package com.isep.psidi.orderservice.domain.yarn;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class YarnType {


    @Id
    private String id;

    private String code;

    private String description;

    public YarnType() {
    }

    public YarnType(String code, String description) {
        this.code = code;
        this.description = description;
        this.id = ObjectId.get().toHexString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
