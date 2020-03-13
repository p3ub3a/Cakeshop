package com.cakeshop.workers;

public class DecorationsConfectioner extends Confectioner {
    public void prepareCake(int duration, int orderId) throws InterruptedException{
        System.out.println("Preparing decorations for order " + orderId);
        Thread.currentThread().sleep(duration);
        System.out.println("Decorations ready for order "+ orderId);
    }
}
