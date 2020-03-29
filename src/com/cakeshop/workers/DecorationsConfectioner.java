package com.cakeshop.workers;

import com.cakeshop.utils.Messages;

public class DecorationsConfectioner extends Confectioner {
    @Override
    public void prepareCake(int duration, int orderId, String cakeName) throws InterruptedException{
        System.out.println(Messages.DECORATIONS_CONFECTIONER_THREAD + Messages.PREPARING_DECORATIONS + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(duration);
        System.out.println(Messages.DECORATIONS_CONFECTIONER_THREAD + Messages.DECORATIONS_READY + orderId + "; cake: " + cakeName);
    }
}
