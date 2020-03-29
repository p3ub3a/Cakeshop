package com.cakeshop.workers;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class Manager {
    public List<Future<Order>> sendOrderToConfectioners(ExecutorService doughCService, ExecutorService creamCService, ExecutorService decosCService, Order order, Cake cake) {
        List<Future<Order>> futures = new ArrayList<>();
        order.setStatus(OrderStatus.WAITING_BAKING);

        Confectioner doughConfectioner = new DoughConfectioner();
        Future<Order> futureDough = sendOrderToConfectioner(doughCService, doughConfectioner, order, cake);
        futures.add(futureDough);

        Confectioner creamConfectioner = new CreamConfectioner();
        Future<Order> futureCream = sendOrderToConfectioner(creamCService, creamConfectioner, order, cake);
        futures.add(futureCream);

        Confectioner decosConfectioner = new DecorationsConfectioner();
        Future<Order> futureDecos = sendOrderToConfectioner(decosCService, decosConfectioner, order, cake);
        futures.add(futureDecos);
        
        return futures;
    }

    private Future<Order> sendOrderToConfectioner(ExecutorService confectionerService, Confectioner confectioner, Order order, Cake cake) {
        return confectionerService.submit(() -> {

            try {
                confectioner.prepareCake(cake.getDoughDuration(), order.getId(), cake.getName());
            } catch (InterruptedException e) {
                order.setStatus(OrderStatus.FAILED);
                e.printStackTrace();
            }
            return order;
        });
    }
}
