package com.cakeshop.workers;

public abstract class Confectioner {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public abstract void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException;

    @Override
    public String toString() {
        return "Confectioner{" +
                "id=" + id +
                '}';
    }
}
