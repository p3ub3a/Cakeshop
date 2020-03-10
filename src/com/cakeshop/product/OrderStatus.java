package com.cakeshop.product;

public enum OrderStatus {
    DONE("done"),
    WAITING_DELIVERY("waiting delivery"),
    WAITING_BAKING("waiting baking"),
    WAITING_MANAGER("waiting manager"),
    FAILED("failed");

    private String status;

    private OrderStatus(String status){
        this.status = status;
    }
}
