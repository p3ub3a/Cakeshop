package com.cakeshop.workers;

public class CreamConfectioner extends Confectioner{
    public void prepareCake(int duration, int orderId) throws InterruptedException{
        System.out.println("Preparing cream for order " + orderId);
        Thread.currentThread().sleep(duration);
        System.out.println("Cream ready for order "+ orderId);
    }
}
