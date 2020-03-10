package com.cakeshop.workers;

import java.util.concurrent.ExecutorService;

public class Manager {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void sendOrder(ExecutorService confectionerService){



    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                '}';
    }
}
