package com.cakeshop.workers;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Manager {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Future<Order> sendOrderToConfectioners(ExecutorService confectionerService, Order order, Cake cake) throws InterruptedException, ExecutionException{
        order.setStatus(OrderStatus.WAITING_BAKING);
        prepareDough(confectionerService, order, cake);
        prepareCream(confectionerService, order, cake);
        Future<Order> futureOrder = prepareDecorations(confectionerService, order, cake);

        return futureOrder;
    }

    private void prepareDough(ExecutorService confectionerService, Order order, Cake cake) {
        confectionerService.submit(() -> {
            Confectioner confectioner = new DoughConfectioner();
            try {
                confectioner.prepareCake(cake.getDoughDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void prepareCream(ExecutorService confectionerService, Order order, Cake cake) {
        confectionerService.submit(() -> {
            Confectioner confectioner = new CreamConfectioner();
            try {
                confectioner.prepareCake(cake.getCreamDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private Future<Order> prepareDecorations(ExecutorService confectionerService, Order order, Cake cake) {
        return confectionerService.submit(() -> {
            Confectioner confectioner = new DecorationsConfectioner();
            try {
                confectioner.prepareCake(cake.getDecorationsDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return order;
        });
    }

    public void deliverCake(Order order, Cake cake, ExecutorService courierService) throws InterruptedException {
        courierService.submit(() -> {
            try{
                order.setStatus(OrderStatus.WAITING_DELIVERY);
                System.out.println("Delivering order " + order.getId() + "; cake: " + cake.getName());
                Thread.currentThread().sleep(cake.getDeliveryDuration());
                System.out.println("Delivered order " + order.getId() + "; cake: " + cake.getName());
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        });
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                '}';
    }
}
