package com.cakeshop.workers;

import com.cakeshop.Runner;
import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Messages;

import java.util.concurrent.ExecutorService;

public class Courier {

    private static volatile int busyCouriers = 0;

    public static synchronized int getBusyCouriers() {
        return busyCouriers;
    }

    public void deliverCake(Order order, Cake cake, ExecutorService courierService, int counter) {
        courierService.submit(() -> {
            synchronized (Runner.monitors[counter]) {
                busyCouriers++;
            }

            try{
                order.setStatus(OrderStatus.WAITING_DELIVERY);
                System.out.println("Delivering order " + order.getId() + "; cake: " + cake.getName());
                Thread.currentThread().sleep(cake.getDeliveryDuration());
                System.out.println("Delivered order " + order.getId() + "; cake: " + cake.getName());
            }catch(InterruptedException e){
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }finally{
                System.out.println(Messages.FREE_COURIER);
                busyCouriers--;
                synchronized (Runner.monitors[counter]){
                    Runner.monitors[counter].setWaiting(false);
                    Runner.monitors[counter].notify();
                }
            }
        });
    }
}
