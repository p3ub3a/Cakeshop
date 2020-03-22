package com.cakeshop.workers;

public class DecorationsConfectioner extends Confectioner {
    @Override
    public void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException{
        System.out.println("Preparing decorations for order " + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(duration);
        System.out.println("Decorations ready for order "+ orderId + "; cake: " + cakeName);
    }
}
