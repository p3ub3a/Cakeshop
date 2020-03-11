package com.cakeshop.workers;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public class Manager {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void sendOrder(ExecutorService confectionerService, Order order, Cake cake) throws InterruptedException{
        order.setStatus(OrderStatus.DONE);
        Thread.sleep(cake.getDeliveryDuration());
        System.out.println("Order " + order.getId() + " has status " + order.getStatus());

    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                '}';
    }
}
