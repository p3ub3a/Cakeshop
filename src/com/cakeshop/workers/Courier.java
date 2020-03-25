package com.cakeshop.workers;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;

import java.util.concurrent.ExecutorService;

public class Courier {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void deliverCake(Order order, Cake cake, ExecutorService courierService) throws InterruptedException {
        courierService.submit(() -> {
            try{
                order.setStatus(OrderStatus.WAITING_DELIVERY);
                System.out.println("Delivering order " + order.getId() + "; cake: " + cake.getName());
                Thread.currentThread().sleep(cake.getDeliveryDuration());
                System.out.println("Delivered order " + order.getId() + "; cake: " + cake.getName());
            }catch(InterruptedException e){
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
        });
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                '}';
    }
}
