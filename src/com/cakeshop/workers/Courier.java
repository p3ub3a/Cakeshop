package com.cakeshop.workers;

import com.cakeshop.Runner;
import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Messages;
import com.cakeshop.utils.Monitor;

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
                System.out.println(Messages.COURIER_THREAD + Messages.DELIVERING + order.getId() + "; cake: " + cake.getName());
                Thread.currentThread().sleep(cake.getDeliveryDuration());
                System.out.println(Messages.COURIER_THREAD + Messages.DELIVERED + order.getId() + "; cake: " + cake.getName());
            }catch(InterruptedException e){
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }finally{
                System.out.println(Messages.COURIER_THREAD + Messages.FREE_COURIER);
                synchronized ( Runner.monitors[counter]){
                    busyCouriers--;
                }
                Monitor.wakeupThread(Runner.monitors[counter]);
            }
        });
    }


}
