package com.cakeshop;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Constants;
import com.cakeshop.workers.*;

import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

public class Runner {
    public static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException, ExecutionException{

        Map<Order, Cake> orderCakeMap = new ConcurrentHashMap<>();

        Queue<Order> orders = new ConcurrentLinkedQueue<Order>();

        // Managers
        ExecutorService managerService = Executors.newFixedThreadPool(Constants.MANAGER_THREAD_NUMBER);
        // Confectioners
        ExecutorService confectionerService = Executors.newFixedThreadPool(Constants.CONFECTIONER_THREAD_NUMBER);
        // Couriers
        ExecutorService courierService = Executors.newFixedThreadPool(Constants.COURIER_THREAD_NUMBER);

        Confectioner doughConfectioner = new DoughConfectioner();
        Confectioner creamConfectioner = new CreamConfectioner();
        Confectioner decorationsConfectioner = new DecorationsConfectioner();

        run(orderCakeMap, orders, managerService, confectionerService, courierService);
    }

    private static void run(Map<Order, Cake> orderCakeMap, Queue<Order> orders, ExecutorService managerService, ExecutorService confectionerService, ExecutorService courierService) throws InterruptedException {


        synchronized (lock){
            boolean shouldRun = true;
            int orderCount = 0;
            System.out.println("Please enter a number associated to the desired cake \n" +
                    "You may press 'X' to quit.\n");

            while (shouldRun) {
                String input;
                Scanner inputScanner = new Scanner(System.in);
                System.out.println("What cake would you like to order ? \n 1. "+ Cake.YELLOW_BUTTER_CAKE.getName() + " \n 2. " + Cake.POUND_CAKE.getName() +
                        "\n 3. " + Cake.RED_VELVET_CAKE.getName() + " \n 4. " + Cake.CARROT_CAKE.getName() + "\n 5. " + Cake.SPONGE_CAKE.getName() +
                        "\n 6. " + Cake.GENOISE_CAKE.getName() + " \n 7. " + Cake.CHIFON_CAKE.getName() + "\n 8. " + Cake.FLOURLESS_CAKE.getName() +
                        "\n 9. " + Cake.UPSIDE_DOWN_CAKE.getName() + " \n 10. " + Cake.HUMMING_BIRD_CAKE.getName() +
                        " \n 11. " + Cake.FRUIT_CAKE.getName() + " \n 12. " + Cake.SIMPLE_CAKE.getName());
                input = inputScanner.nextLine();
                if(input.equals("X")){
                    shouldRun=false;
                    break;
                }

                if (input.matches("[1-9]|1[0-2]")) {
                    orderCount++;
                    Cake cake = Cake.getCakeById(Integer.parseInt(input));
                    Order order = new Order();
                    order.setId(orderCount);
                    order.setStatus(OrderStatus.WAITING_MANAGER);

                    orders.add(order);
                    orderCakeMap.put(order, cake);

                    if(orders.size() <= 4){
                        managerService.submit(() -> {

                            System.out.println("on thread: " + Thread.currentThread().getName());
                            System.out.println(cake.toString());

                            Manager manager = new Manager();
                            try {
                                manager.sendOrder(confectionerService, order, cake);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            orders.remove(order);
                            System.out.println("----------- size: " + orders.size());
                            synchronized (lock){
                                // lock.notify();
                            }
                        });

                    }else{
                        System.out.println("\n Please stand by as we assign your order to a manager ... \n");
                        System.out.println(Thread.currentThread().getName());
                        //lock.wait();
                    }
                } else {
                    System.out.println("\nPlease enter a numeric value in the range 1-12\n");
                    continue;
                }
            }

            System.out.println("Shutting down executors...");
            managerService.shutdown();
            confectionerService.shutdown();
            courierService.shutdown();
        }

    }
}