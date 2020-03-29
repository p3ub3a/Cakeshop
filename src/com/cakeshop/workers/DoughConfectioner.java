package com.cakeshop.workers;

import com.cakeshop.utils.Messages;

public class DoughConfectioner extends Confectioner {
    @Override
    public void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException{
        System.out.println(Messages.DOUGH_CONFECTIONER_THREAD + Messages.PREPARING_DOUGH + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(duration);
        System.out.println(Messages.DOUGH_CONFECTIONER_THREAD + Messages.DOUGH_READY + orderId + "; cake: " + cakeName);
    }

}
