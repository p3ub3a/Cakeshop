package com.cakeshop.utils;

public class Messages {

    // main thread
    public static final String MAIN_THREAD = ">>>>> SERVICE LINE: ";
    public static final String INTRO = "Please enter a number associated to the desired cake \n" +
            "You may type 'X' to quit.\n";
    public static final String IN_THE_RANGE_1_12 = "Please enter a numeric value in the range 1-12\n";
    public static final String ORDER_TO_A_MANAGER = " Please stand by as all managers are currently busy ... \n";
    public static final String MANAGER_READY = " A manager is ready to take your order \n";
    public static final String SHUTTING_DOWN_EXECUTORS = "Closing shop...";
    public static final String CAKE_ORDER = "What cake would you like to order ?";

    //managers
    public static final String MANAGER_THREAD = "+++++ MANAGER : ";
    public static final String ASSIGN_ORDER = "A manager got the following cake: ";
    public static final String WAITING_MANAGER = "A manager is  waiting for a courier to be free for order ";
    public static final String RESUMING_MANAGER = "A manager resumed work for order ";
    public static final String FREE_MANAGER = "One manager is free!";

    // confectioners
    public static final String DOUGH_CONFECTIONER_THREAD = "----- DOUGH CONFECTIONER : ";
    public static final String CREAM_CONFECTIONER_THREAD = "----- CREAM CONFECTIONER : ";
    public static final String DECORATIONS_CONFECTIONER_THREAD = "----- DECORATIONS CONFECTIONER : ";
    public static final String PREPARING_CREAM = "Preparing cream for order ";
    public static final String PREPARING_DOUGH = "Preparing dough for order ";
    public static final String PREPARING_DECORATIONS = "Preparing decorations for order ";
    public static final String CREAM_READY = "Cream ready for order ";
    public static final String DOUGH_READY = "Dough ready for order ";
    public static final String DECORATIONS_READY = "Decorations ready for order ";

    // couriers
    public static final String COURIER_THREAD = "<<<<< COURIER : ";
    public static final String DELIVERING = "Delivering order ";
    public static final String FREE_COURIER = "One courier is free!";
}
