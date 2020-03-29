package com.cakeshop.workers;

public abstract class Confectioner {
    private int duration;

    public Confectioner(int duration){
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public abstract void prepareCake(int orderId, String cakeName) throws InterruptedException;
}
