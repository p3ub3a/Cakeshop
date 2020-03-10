package com.cakeshop.workers;

public abstract class Confectioner {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Confectioner{" +
                "id=" + id +
                '}';
    }
}
