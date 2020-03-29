package com.cakeshop.workers;

import com.cakeshop.utils.Messages;

public class CreamConfectioner extends Confectioner{
    public CreamConfectioner(int duration){
        super(duration);
    }

    @Override
    public void prepareCake(int orderId, String cakeName) throws InterruptedException{
        System.out.println(Messages.CREAM_CONFECTIONER_THREAD + Messages.PREPARING_CREAM + orderId + "; cake: " + cakeName);
        Thread.currentThread().sleep(super.getDuration());
        System.out.println(Messages.CREAM_CONFECTIONER_THREAD + Messages.CREAM_READY + orderId + "; cake: " + cakeName);
    }
}
