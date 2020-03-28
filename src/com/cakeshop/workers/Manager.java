package com.cakeshop.workers;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Manager {
    public List<Future<Order>> sendOrderToConfectioners(ExecutorService confectionerService, Order order, Cake cake) throws InterruptedException, ExecutionException{
        List<Future<Order>> futures = new ArrayList<>();
        order.setStatus(OrderStatus.WAITING_BAKING);
        Future<Order> futureDough = sendOrderToDoughConfectioner(confectionerService, order, cake);
        futures.add(futureDough);
        Future<Order> futureCream = sendOrderToCreamConfectioner(confectionerService, order, cake);
        futures.add(futureCream);
        Future<Order> futureDecos = sendOrderToDecorationsConfectioner(confectionerService, order, cake);
        futures.add(futureDecos);
        
        return futures;
    }

    private Future<Order> sendOrderToDoughConfectioner(ExecutorService confectionerService, Order order, Cake cake) {
        return confectionerService.submit(() -> {
            Confectioner confectioner = new DoughConfectioner();
            try {
                confectioner.prepareCake(cake.getDoughDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
            return order;
        });
    }

    private Future<Order> sendOrderToCreamConfectioner(ExecutorService confectionerService, Order order, Cake cake) {
        return confectionerService.submit(() -> {
            Confectioner confectioner = new CreamConfectioner();
            try {
                confectioner.prepareCake(cake.getCreamDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
            return order;
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
}
