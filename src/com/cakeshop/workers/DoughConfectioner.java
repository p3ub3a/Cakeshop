package com.cakeshop.workers;

import com.cakeshop.product.Order;

public class DoughConfectioner extends Confectioner {
    @Override
    public void prepareCake(int duration, int orderId) throws InterruptedException{
        System.out.println("Preparing dough for order " + orderId);
        Thread.currentThread().sleep(duration);
        System.out.println("Dough ready for order "+ orderId);
    }

}
