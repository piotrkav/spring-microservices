package com.isep.psidi.orderservice.domain.order;


import com.isep.psidi.orderservice.domain.fabric.Fabric;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
public class Order {

    @Id
    private String id;
    private String customerId;
    private String supplierId;
    private Date createdDate;
    private Date currentProcessingStartDate;
    private OrderStatus orderStatus;
    private List<OrderLine> orderLines;
    private Fabric fabric;
    private boolean isDyed;
    private boolean isStamped;
    private double quantity;
    private double materialPrice;
    private double knittingPrice;
    private double pretreatmentPrice;
    private double dyeingPrice;
    private double stampingPrice;
    private double finishingPrice;
    private double inspectionPrice;
    private double packingPrice;
    private double totalPrice;

    public double getKnittingPrice() {
        return knittingPrice;
    }

    public void setKnittingPrice(double knittingPrice) {
        this.knittingPrice = knittingPrice;
    }

    public double getDyeingPrice() {
        return dyeingPrice;
    }

    public void setDyeingPrice(double dyeingPrice) {
        this.dyeingPrice = dyeingPrice;
    }

    public double getStampingPrice() {
        return stampingPrice;
    }

    public void setStampingPrice(double stampingPrice) {
        this.stampingPrice = stampingPrice;
    }

    public double getFinishingPrice() {
        return finishingPrice;
    }

    public void setFinishingPrice(double finishingPrice) {
        this.finishingPrice = finishingPrice;
    }

    public double getInspectionPrice() {
        return inspectionPrice;
    }

    public void setInspectionPrice(double inspectionPrice) {
        this.inspectionPrice = inspectionPrice;
    }

    public double getPackingPrice() {
        return packingPrice;
    }

    public void setPackingPrice(double packingPrice) {
        this.packingPrice = packingPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    public Fabric getFabric() {
        return fabric;
    }

    public void setFabric(Fabric fabric) {
        this.fabric = fabric;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCurrentProcessingStartDate() {
        return currentProcessingStartDate;
    }

    public void setCurrentProcessingStartDate(Date currentProcessingStartDate) {
        this.currentProcessingStartDate = currentProcessingStartDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getPretreatmentPrice() {
        return pretreatmentPrice;
    }

    public void setPretreatmentPrice(double pretreatmentPrice) {
        this.pretreatmentPrice = pretreatmentPrice;
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

    public double getMaterialPrice() {
        return materialPrice;
    }

    public void setMaterialPrice(double materialPrice) {
        this.materialPrice = materialPrice;
    }
}
