package com.cakeshop;

import com.cakeshop.product.Cake;
import com.cakeshop.product.Order;
import com.cakeshop.product.OrderStatus;
import com.cakeshop.utils.Constants;
import com.cakeshop.utils.Messages;
import com.cakeshop.utils.Monitor;
import com.cakeshop.workers.*;

import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

public class Runner {
    private static boolean shouldRun = true;

    private static int orderCount = 0;

    private static Monitor queueMonitor = new Monitor();

    public static Monitor managerMonitor = new Monitor();

    private static int monitorCounter = 0;

    public static void main(String[] args) throws InterruptedException{

        Queue<Order> orders = new ConcurrentLinkedQueue<>();

        // Managers
        ExecutorService managerService = Executors.newFixedThreadPool(Constants.MANAGER_THREAD_NUMBER);
        // Confectioners
        ExecutorService doughConfectionerService = Executors.newFixedThreadPool(Constants.DOUGH_CONFECTIONER_THREAD_NUMBER);
        ExecutorService creamConfectionerService = Executors.newFixedThreadPool(Constants.CREAM_CONFECTIONER_THREAD_NUMBER);
        ExecutorService decosConfectionerService = Executors.newFixedThreadPool(Constants.DECOS_CONFECTIONER_THREAD_NUMBER);
        // Couriers
        ExecutorService courierService = Executors.newFixedThreadPool(Constants.COURIER_THREAD_NUMBER);

        simulateShop(orders, managerService, doughConfectionerService, creamConfectionerService, decosConfectionerService, courierService);
    }

    private static void simulateShop(Queue<Order> orders, ExecutorService managerService, ExecutorService doughCService, ExecutorService creamCService, ExecutorService decosCService, ExecutorService courierService) throws InterruptedException {
        System.out.println(Messages.MAIN_THREAD + Messages.INTRO);
        while (shouldRun) {
            if(orders.size() <= Constants.QUEUE_SIZE){
                String input = getInput();

                if (input == null) break;

                if (input.matches(Constants.INPUT_REGEX)) {
                    Cake cake = Cake.getCakeById(Integer.parseInt(input));

                    Order order = createOrder(orders);

                    managerService.submit(() -> {
                        processOrder(orders, doughCService, creamCService, decosCService, courierService, cake, order);
                    });

                    order.setStatus(OrderStatus.DONE);
                } else {
                    System.out.println(Messages.MAIN_THREAD + Messages.IN_THE_RANGE_1_12);
                    continue;
                }
            }else{
                System.out.println(Messages.MAIN_THREAD + Messages.ORDER_TO_A_MANAGER);
                queueMonitor.setWaiting(true);
                Monitor.pauseThread(queueMonitor);
                System.out.println(Messages.MAIN_THREAD + Messages.MANAGER_READY);
            }
        }

        System.out.println(Messages.MAIN_THREAD + Messages.SHUTTING_DOWN_EXECUTORS);
        managerService.shutdown();
        doughCService.shutdown();
        creamCService.shutdown();
        decosCService.shutdown();
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
        messageBuilder.append(Messages.MAIN_THREAD).append(Messages.CAKE_ORDER)
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

    public static void processOrder(Queue<Order> orders, ExecutorService doughCService, ExecutorService creamCService, ExecutorService decosCService, ExecutorService courierService, Cake cake, Order order) {
        System.out.println(Messages.MANAGER_THREAD + Messages.ASSIGN_ORDER + cake.toString());

        orders.remove(order);
        // in case service line is waiting, notify it that an order is ready to be taken
        if(orders.size() <= Constants.QUEUE_SIZE){
            Monitor.wakeupThread(queueMonitor);
        }

        int counter = updateMonitorCounter();

        try {
            Manager manager = new Manager();

            List<Future<Order>> futures = manager.sendOrderToConfectioners(doughCService, creamCService, decosCService, order, cake);
            order = retrieveOrder(futures);

            if(Courier.getBusyCouriers() == Constants.COURIER_THREAD_NUMBER){
                System.out.println(Messages.MANAGER_THREAD + Messages.WAITING_MANAGER + order.getId());
                Monitor.pauseThread(managerMonitor);
                System.out.println(Messages.MANAGER_THREAD + Messages.RESUMING_MANAGER + order.getId());
            }

            Courier courier = new Courier();
            courier.deliverCake(order, cake, courierService, counter);
            System.out.println(Messages.MANAGER_THREAD + Messages.FREE_MANAGER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static int updateMonitorCounter() {
        if(monitorCounter >= Constants.COURIER_THREAD_NUMBER ){
            monitorCounter=0;
        }
        return monitorCounter++;
    }

    private static Order retrieveOrder(List<Future<Order>> futures){
        Order order = null;
        for(Future<Order> futureOrder: futures){
            try {
                order = futureOrder.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return order;
    }
}