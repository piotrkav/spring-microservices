package com.isep.psidi.orderservice.domain.order;

public enum OrderStatus {
    PENDING,
    PROCESSING_KNITTING,
    PROCESSING_PRETREATMENT,
    PROCESSING_DYEING,
    PROCESSING_STAMPING,
    PROCESSING_FINISHING,
    PROCESSING_INSPECTION,
    PROCESSING_PACKING,
    FINISHED
}
