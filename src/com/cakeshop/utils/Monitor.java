package com.cakeshop.utils;

public class Monitor {
    private boolean waiting;

    public synchronized boolean isWaiting() {
        return waiting;
    }

    public synchronized void setWaiting(boolean waiting) {
        this.waiting = waiting;
    }

    public static void pauseThread(Monitor monitor) throws InterruptedException {
        monitor.setWaiting(true);
        synchronized (monitor){
            // keep on waiting in case of spurious wakeups
            while(monitor.isWaiting()){
                monitor.setWaiting(true);
                monitor.wait();
            }
        }
    }

    public static void wakeupThread(Monitor monitor) {
        synchronized (monitor){
            monitor.setWaiting(false);
            monitor.notify();
        }
    }
}
