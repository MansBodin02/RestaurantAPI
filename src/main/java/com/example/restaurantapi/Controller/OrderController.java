package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Order.OrderItem;
import com.example.restaurantapi.Repo.FoodRepo;
import com.example.restaurantapi.Repo.OrderRepo;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepo orderRepo;

    private final FoodRepo foodRepo;

    public OrderController(OrderRepo orderRepo, FoodRepo foodRepo) {
        this.orderRepo = orderRepo;
        this.foodRepo = foodRepo;
    }

    @GetMapping("/")
    public List<CustomerOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    @GetMapping("/{orderTable}")
    public CustomerOrder getOrdersByTable(@PathVariable int orderTable) {
        return orderRepo.findOrderByOrderTable(orderTable);
    }

    @PostMapping("/")
    public String saveCustomerOrder(@RequestBody CustomerOrder customerOrder) {
        try {
            List<OrderItem> orderItems = new ArrayList<>();

            for (OrderItem item : customerOrder.getOrderItems()) {
                Food food = foodRepo.findFoodByFoodName(item.getFood().getFoodName())
                        .orElseThrow(() -> new IllegalArgumentException("Food not found: " + item.getFood().getFoodName()));

                // Här skapar vi OrderItem med specialinstruktioner
                orderItems.add(new OrderItem(customerOrder, food, item.getFoodQuantity(), item.getOrderItemSpecial()));
            }

            customerOrder.setOrderItems(orderItems);

            // Beräkna totalpris
            double totalPrice = orderItems.stream()
                    .mapToDouble(item -> item.getFood().getFoodPrice() * item.getFoodQuantity())
                    .sum();
            customerOrder.setOrderPrice(totalPrice);

            orderRepo.save(customerOrder);
            return "Order saved with total price: " + totalPrice;

        } catch (Exception e) {
            return "Error saving order: " + e.getMessage();
        }
    }




    @PostMapping("/batch")
    public String saveCustomerOrders(@RequestBody List<CustomerOrder> customerOrders) {
        List<String> errors = new ArrayList<>();

        // Bearbeta varje order
        for (CustomerOrder customerOrder : customerOrders) {
            try {
                // Hämta foodItems baserat på foodName istället för foodId
                List<OrderItem> orderItems = customerOrder.getOrderItems().stream()
                        .map(orderItem -> {
                            // Hitta maten baserat på foodName istället för foodId
                            Food food = foodRepo.findFoodByFoodName(orderItem.getFood().getFoodName())
                                    .orElseThrow(() -> new IllegalArgumentException("Food not found: " + orderItem.getFood().getFoodName()));

                            // Skapa nytt OrderItem med specialinstruktioner
                            return new OrderItem(customerOrder, food, orderItem.getFoodQuantity(), orderItem.getOrderItemSpecial());
                        })
                        .collect(Collectors.toList());

                customerOrder.setOrderItems(orderItems);

                // Beräkna totalpris för ordern
                double totalPrice = orderItems.stream()
                        .mapToDouble(item -> item.getFood().getFoodPrice() * item.getFoodQuantity())
                        .sum();
                customerOrder.setOrderPrice(totalPrice);

                orderRepo.save(customerOrder);
            } catch (Exception e) {
                errors.add("Error with order " + customerOrder.getOrderItems() + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            return "Errors occurred: " + String.join(", ", errors);
        } else {
            return "All orders saved successfully!";
        }
    }




    @PutMapping("/{orderTable}")
    public String updateOrder(@PathVariable int orderTable, @RequestBody CustomerOrder customerOrder) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        try {
            // Bearbeta varje OrderItem för att uppdatera maten och specialinstruktioner
            List<OrderItem> orderItems = customerOrder.getOrderItems().stream()
                    .map(orderItem -> {
                        Food food = foodRepo.findFoodByFoodId(orderItem.getFood().getFoodId())
                                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + orderItem.getFood().getFoodId()));
                        return new OrderItem(existingCustomerOrder, food, orderItem.getFoodQuantity(), orderItem.getOrderItemSpecial());
                    })
                    .collect(Collectors.toList());

            existingCustomerOrder.setOrderItems(orderItems);
            existingCustomerOrder.setOrderTable(customerOrder.getOrderTable());
            existingCustomerOrder.setOrderDateTime(customerOrder.getOrderDateTime());
            existingCustomerOrder.setOrderPrice(customerOrder.getOrderPrice());
            existingCustomerOrder.setOrderState(customerOrder.getOrderState());

            orderRepo.save(existingCustomerOrder);
            return "Order updated successfully!";
        } catch (Exception e) {
            return "Error updating order: " + e.getMessage();
        }
    }



    @PutMapping("/{orderTable}/addFood/{foodName}")
    public String addFoodToOrder(@PathVariable int orderTable, @PathVariable String foodName, @RequestParam String orderItemSpecial) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Food foodToAdd = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        // Kontrollera om maten redan finns i ordern
        boolean foodExists = existingCustomerOrder.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.getFood().equals(foodToAdd));

        if (foodExists) {
            return "Food " + foodName + " is already in this order.";
        }

        // Lägg till OrderItem med specialinstruktioner
        OrderItem orderItem = new OrderItem(existingCustomerOrder, foodToAdd, 1, orderItemSpecial);  // 1 är en default mängd
        existingCustomerOrder.getOrderItems().add(orderItem);

        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> item.getFood().getFoodPrice() * item.getFoodQuantity())
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Food " + foodName + " added to order and order price updated to " + newOrderPrice;
    }


    @PutMapping("/{orderTable}/removeFood/{foodName}")
    public String removeFoodFromOrder(@PathVariable int orderTable, @PathVariable String foodName) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Food foodToRemove = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        OrderItem orderItemToRemove = existingCustomerOrder.getOrderItems().stream()
                .filter(orderItem -> orderItem.getFood().equals(foodToRemove))
                .findFirst()
                .orElse(null);

        if (orderItemToRemove == null) {
            return "Food " + foodName + " is not in this order.";
        }

        existingCustomerOrder.getOrderItems().remove(orderItemToRemove);

        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> item.getFood().getFoodPrice() * item.getFoodQuantity())
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Food " + foodName + " removed from order and order price updated to " + newOrderPrice;
    }



    @DeleteMapping("/{orderTable}")
    public String deleteOrder(@PathVariable int orderTable) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        orderRepo.delete(existingCustomerOrder);
        return "Order deleted successfully!";
    }

}
