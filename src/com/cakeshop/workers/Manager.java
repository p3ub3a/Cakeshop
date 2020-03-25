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
        sendOrderToDoughConfectioner(confectionerService, order, cake);
        sendOrderToCreamConfectioner(confectionerService, order, cake);
        Future<Order> futureOrder = sendOrderToDecorationsConfectioner(confectionerService, order, cake);

        return futureOrder;
    }

    private void sendOrderToDoughConfectioner(ExecutorService confectionerService, Order order, Cake cake) {
        confectionerService.submit(() -> {
            Confectioner confectioner = new DoughConfectioner();
            try {
                confectioner.prepareCake(cake.getDoughDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
        });
    }

    private void sendOrderToCreamConfectioner(ExecutorService confectionerService, Order order, Cake cake) {
        confectionerService.submit(() -> {
            Confectioner confectioner = new CreamConfectioner();
            try {
                confectioner.prepareCake(cake.getCreamDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
        });
    }

    private Future<Order> sendOrderToDecorationsConfectioner(ExecutorService confectionerService, Order order, Cake cake) {
        return confectionerService.submit(() -> {
            Confectioner confectioner = new DecorationsConfectioner();
            try {
                confectioner.prepareCake(cake.getDecorationsDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
            return order;
        });
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                '}';
    }
}
