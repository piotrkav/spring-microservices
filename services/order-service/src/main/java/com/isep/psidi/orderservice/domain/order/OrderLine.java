package com.isep.psidi.orderservice.domain.order;

import com.isep.psidi.orderservice.domain.yarn.YarnItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrderLine {


    @Id
    private String id;
    private String orderId;
    private String fabricId;
    private YarnItem yarnItem;
    private double quantity;
    private int numberOfCables;
    private int numberOfFilaments;
    private int NETitle;
    private int TMP;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFabricId() {
        return fabricId;
    }

    public void setFabricId(String fabricId) {
        this.fabricId = fabricId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getNumberOfCables() {
        return numberOfCables;
    }

    public void setNumberOfCables(int numberOfCables) {
        this.numberOfCables = numberOfCables;
    }

    public int getNumberOfFilaments() {
        return numberOfFilaments;
    }

    public void setNumberOfFilaments(int numberOfFilaments) {
        this.numberOfFilaments = numberOfFilaments;
    }

    public int getNETitle() {
        return NETitle;
    }

    public void setNETitle(int NETitle) {
        this.NETitle = NETitle;
    }

    public int getTMP() {
        return TMP;
    }

    public void setTMP(int TMP) {
        this.TMP = TMP;
    }

    public YarnItem getYarnItem() {
        return yarnItem;
    }

    public void setYarnItem(YarnItem yarnItem) {
        this.yarnItem = yarnItem;
    }
}
