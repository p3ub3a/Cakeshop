package com.cakeshop;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Constants;
import com.cakeshop.workers.*;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

public class Runner {
    private static boolean shouldRun = true;

    private static volatile int orderCount = 0;

    public static void main(String[] args) throws InterruptedException, ExecutionException{

        Queue<Order> orders = new ConcurrentLinkedQueue<>();

        // Managers
        ExecutorService managerService = Executors.newFixedThreadPool(Constants.MANAGER_THREAD_NUMBER);
        // Confectioners
        ExecutorService confectionerService = Executors.newFixedThreadPool(Constants.CONFECTIONER_THREAD_NUMBER);
        // Couriers
        ExecutorService courierService = Executors.newFixedThreadPool(Constants.COURIER_THREAD_NUMBER);

        simulateShop(orders, managerService, confectionerService, courierService);
    }

    private static void simulateShop(Queue<Order> orders, ExecutorService managerService, ExecutorService confectionerService, ExecutorService courierService) throws InterruptedException {

        System.out.println("Please enter a number associated to the desired cake \n" +
                "You may press 'X' to quit.\n");

        while (shouldRun) {
            if(orders.size() < Constants.MANAGER_THREAD_NUMBER){
                String input = getInput();

                if (input == null) break;

                if (input.matches(Constants.INPUT_REGEX)) {
                    Cake cake = Cake.getCakeById(Integer.parseInt(input));

                    Order order = createOrder(orders);

                    managerService.submit(() -> {
                        processOrder(orders, confectionerService, courierService, cake, order);
                    });
                } else {
                    System.out.println("\nPlease enter a numeric value in the range 1-12\n");
                    continue;
                }
            }else{
                System.out.println("\n Please stand by as we assign your order to a manager ... \n");
                Thread.sleep(Constants.STAND_BY_TIME);

            }
        }

        System.out.println("Shutting down executors...");
        managerService.shutdown();
        confectionerService.shutdown();
        courierService.shutdown();
    }

    private static String getInput() {
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
            return null;
        }
        return input;
    }

    private static Order createOrder(Queue<Order> orders) {
        Order order = new Order();
        orderCount++;
        order.setId(orderCount);
        order.setStatus(OrderStatus.WAITING_MANAGER);

        orders.add(order);
        return order;
    }

    private static void processOrder(Queue<Order> orders, ExecutorService confectionerService, ExecutorService courierService, Cake cake, Order order) {
        System.out.println("A manager got this cake: " + cake.toString());

        Manager manager = new Manager();
        try {
            Future<Order> futureOrder = manager.sendOrderToConfectioners(confectionerService, order, cake);
            while(!futureOrder.isDone()){
                Thread.currentThread().sleep(Constants.STAND_BY_TIME);
            }
            manager.deliverCake(futureOrder.get(), cake, courierService);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        orders.remove(order);
    }
}