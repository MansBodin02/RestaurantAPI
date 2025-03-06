package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Order.OrderCategory;
import com.example.restaurantapi.Models.Order.OrderItem;
import com.example.restaurantapi.Models.Order.OrderState;
import com.example.restaurantapi.Repo.DrinkRepo;
import com.example.restaurantapi.Repo.FoodRepo;
import com.example.restaurantapi.Repo.OrderRepo;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * OrderController - A REST API controller for managing customer orders in the restaurant system.
 * Provides endpoints to create, retrieve, update, and delete orders.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepo orderRepo;
    private final FoodRepo foodRepo;
    private final DrinkRepo drinkRepo;

    public OrderController(OrderRepo orderRepo, FoodRepo foodRepo, DrinkRepo drinkRepo) {
        this.orderRepo = orderRepo;
        this.foodRepo = foodRepo;
        this.drinkRepo = drinkRepo;
    }

    /**
     * Retrieve all customer orders from the database.
     * @return List of all customer orders.
     */
    @GetMapping("/")
    public List<CustomerOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    /**
     * Retrieve a customer order by its ID.
     * @param orderId The unique order ID.
     * @return The customer order if found.
     */
    @GetMapping("/orderId/{orderId}")
    public CustomerOrder getOrderById(@PathVariable Long orderId) {
        return orderRepo.findOrderByOrderId(orderId);
    }

    /**
     * Retrieve orders by category.
     * @param orderCategory The order category.
     * @return List of customer orders matching the category.
     */
    @GetMapping("/orderCategory/{orderCategory}")
    public List<CustomerOrder> getOrdersByOrderCategory(@PathVariable OrderCategory orderCategory) {
        return orderRepo.findCustomerOrdersByOrderCategory(orderCategory);
    }

    /**
     * Retrieve orders by table number.
     * @param orderTable The table number.
     * @return List of customer orders for the specified table.
     */
    @GetMapping("/orderTable/{orderTable}")
    public List<CustomerOrder> getOrdersByTable(@PathVariable int orderTable) {
        return orderRepo.findOrdersByOrderTable(orderTable);
    }

    /**
     * Retrieve orders by their current state.
     * @param orderState The state of the orders.
     * @return List of orders matching the state.
     */
    @GetMapping("/orderState/{orderState}")
    public List<CustomerOrder> getOrdersByState(@PathVariable OrderState orderState) {
        return orderRepo.findOrdersByOrderState(orderState);
    }

    /**
     * Retrieve an order by its timestamp.
     * @param orderDateTime The timestamp of the order.
     * @return The customer order if found.
     */
    @GetMapping("/orderDateTime/{orderDateTime}")
    public CustomerOrder getOrderByDateTime(@PathVariable LocalDateTime orderDateTime) {
        return orderRepo.findOrderByOrderDateTime(orderDateTime);
    }

    /**
     * Delete all orders for a specific table.
     * @param orderTable The table number.
     * @return Success message.
     */
    @DeleteMapping("/orderTable/{orderTable}")
    public String deleteOrders(@PathVariable int orderTable) {
        List<CustomerOrder> existingOrders = orderRepo.findOrdersByOrderTable(orderTable);

        if (existingOrders.isEmpty()) {
            return "No orders found at table: " + orderTable;
        }

        orderRepo.deleteAll(existingOrders);
        return "All orders at table " + orderTable + " deleted successfully!";
    }

    /**
     * Delete a specific order by its ID.
     * @param orderId The unique order ID.
     * @return Success message.
     */
    @DeleteMapping("/orderId/{orderId}")
    public String deleteOrder(@PathVariable Long orderId) {
        CustomerOrder existingOrder = orderRepo.findOrderByOrderId(orderId);
        orderRepo.delete(existingOrder);
        return "Order with order ID " + orderId + " deleted successfully!";
    }
}
