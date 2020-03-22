package com.cakeshop.workers;

public class DoughConfectioner extends Confectioner {
    @Override
    public void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException{
        System.out.println("Preparing dough for order " + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(duration);
        System.out.println("Dough ready for order "+ orderId + "; cake: " + cakeName);
    }

}
