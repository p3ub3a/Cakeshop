package com.cakeshop;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Constants;
import com.cakeshop.workers.*;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Runner {

    public static void main(String[] args) throws InterruptedException{

        // Managers
        ExecutorService managerService = Executors.newFixedThreadPool(Constants.MANAGER_THREAD_NUMBER);
        // Confectioners
        ExecutorService confectionerService = Executors.newFixedThreadPool(Constants.CONFECTIONER_THREAD_NUMBER);
        // Couriers
        ExecutorService courierService = Executors.newFixedThreadPool(Constants.COURIER_THREAD_NUMBER);

        Confectioner doughConfectioner = new DoughConfectioner();
        Confectioner creamConfectioner = new CreamConfectioner();
        Confectioner decorationsConfectioner = new DecorationsConfectioner();

        Queue<Order> orders = new ConcurrentLinkedQueue<Order>();

        boolean shouldRun = true;
        int orderCount = 0;
        System.out.println("Please enter a number associated to the desired cake \n" +
                "You may press 'X' to quit.\n");

        while (shouldRun) {
            String input;
            Scanner inputScanner = new Scanner(System.in);
            System.out.println("What cake would you like to order ? \n 1. "+ Cake.YELLOW_BUTTER_CAKE + " \n 2. " + Cake.POUND_CAKE +
                    "\n 3. " + Cake.RED_VELVET_CAKE + " \n 4. " + Cake.CARROT_CAKE + "\n 5. " + Cake.SPONGE_CAKE + "\n 6. " + Cake.GENOISE_CAKE +
                    " \n 7. " + Cake.CHIFON_CAKE + "\n 8. " + Cake.FLOURLESS_CAKE + "\n 9. " + Cake.UPSIDE_DOWN_CAKE + " \n 10. " + Cake.HUMMING_BIRD_CAKE +
                    " \n 11. " + Cake.FRUIT_CAKE + " \n 12. " + Cake.SIMPLE_CAKE);
            input = inputScanner.nextLine();
            if(input.equals("X")){
                shouldRun=false;
                break;
            }

            if (input.matches("[1-9]|1[0-2]")) {
                Cake currentCake = Cake.getCakeById(Integer.parseInt(input));
                Order order = new Order();
                order.setId(orderCount);
                order.setStatus(OrderStatus.WAITING_MANAGER);

                if(orders.size() < 4){
                    orders.add(order);
                }else{
                    Thread.currentThread().wait();
                }


                System.out.println("\n Please stand by as we assign the order to a manager ... \n");
                Thread.currentThread().sleep(Constants.STAND_BY_TIME);

                Future<Order> managerOrderFuture = managerService.submit(() -> {
                    System.out.println("on thread: " + Thread.currentThread().getName());
                    System.out.println(currentCake.toString());

                    Manager manager = new Manager();
                    manager.sendOrder(confectionerService);

                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return order;
                });
            } else {
                System.out.println("\nPlease enter a numeric value in the range 1-12\n");
                continue;
            }

            orderCount++;
        }
    }
}