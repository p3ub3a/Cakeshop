package com.cakeshop.workers;

public class CreamConfectioner extends Confectioner{
    @Override
    public void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException{
        System.out.println("Preparing cream for order " + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(duration);
        System.out.println("Cream ready for order "+ orderId + "; cake: " + cakeName);
    }
}
