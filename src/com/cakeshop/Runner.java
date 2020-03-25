package com.cakeshop;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Constants;
import com.cakeshop.utils.Messages;
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

        System.out.println(Messages.INTRO_MESSAGE);

        while (shouldRun) {
            if(orders.size() <= Constants.MANAGER_THREAD_NUMBER){
                String input = getInput();

                if (input == null) break;

                if (input.matches(Constants.INPUT_REGEX)) {
                    Cake cake = Cake.getCakeById(Integer.parseInt(input));

                    Order order = createOrder(orders);

                    managerService.submit(() -> {
                        processOrder(orders, confectionerService, courierService, cake, order);
                    });

                    order.setStatus(OrderStatus.DONE);
                } else {
                    System.out.println(Messages.IN_THE_RANGE_1_12_MESSAGE);
                    continue;
                }
            }else{
                System.out.println(Messages.ORDER_TO_A_MANAGER_MESSAGE);
                Thread.sleep(Constants.STAND_BY_TIME);

            }
        }

        System.out.println(Messages.SHUTTING_DOWN_EXECUTORS_MESSAGE);
        managerService.shutdown();
        confectionerService.shutdown();
        courierService.shutdown();
    }

    private static String getInput() {
        String input;
        Scanner inputScanner = new Scanner(System.in);
        displayMessage();
        input = inputScanner.nextLine();
        if(input.equals("X")){
            shouldRun=false;
            return null;
        }
        return input;
    }

    private static void displayMessage() {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(Messages.CAKE_ORDER_MESSAGE)
                .append(" \n 1. ").append(Cake.YELLOW_BUTTER_CAKE.getName())
                .append(" \n 2. ").append(Cake.POUND_CAKE.getName())
                .append(" \n 3. ").append(Cake.RED_VELVET_CAKE.getName())
                .append(" \n 4. ").append(Cake.CARROT_CAKE.getName())
                .append(" \n 5. ").append(Cake.SPONGE_CAKE.getName())
                .append(" \n 6. ").append(Cake.GENOISE_CAKE.getName())
                .append(" \n 7. ").append(Cake.CHIFON_CAKE.getName())
                .append(" \n 8. ").append(Cake.FLOURLESS_CAKE.getName())
                .append(" \n 9. ").append(Cake.UPSIDE_DOWN_CAKE.getName())
                .append(" \n 10. ").append(Cake.HUMMING_BIRD_CAKE.getName())
                .append(" \n 11. ").append(Cake.FRUIT_CAKE.getName())
                .append(" \n 12. ").append(Cake.SIMPLE_CAKE.getName());
        System.out.println(messageBuilder);
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
        System.out.println("A manager got the following cake: " + cake.toString());

        Manager manager = new Manager();
        Courier courier = new Courier();
        try {
            Future<Order> futureOrder = manager.sendOrderToConfectioners(confectionerService, order, cake);
            while(!futureOrder.isDone()){
                Thread.currentThread().sleep(Constants.STAND_BY_TIME);
            }
            courier.deliverCake(futureOrder.get(), cake, courierService);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        orders.remove(order);
    }
}