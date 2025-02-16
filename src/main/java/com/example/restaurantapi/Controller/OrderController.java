package com.example.restaurantapi.Controller;

import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Repo.FoodRepo;
import com.example.restaurantapi.Repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private FoodRepo foodRepo;

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
            // Konvertera foodNames till riktiga Food-objekt
            List<Food> foodList = customerOrder.getFoodOrders().stream()
                    .map(food -> foodRepo.findFoodByFoodName(food.getFoodName())
                            .orElseThrow(() -> new IllegalArgumentException("Food not found: " + food.getFoodName())))
                    .collect(Collectors.toList());

            customerOrder.setFoodOrders(foodList);

            // Beräkna totalpris för ordern
            double totalPrice = foodList.stream().mapToDouble(Food::getFoodPrice).sum();
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

        // Samla alla foodNames från alla ordrar
        Set<String> foodNames = customerOrders.stream()
                .flatMap(order -> order.getFoodOrders().stream())
                .map(Food::getFoodName)
                .collect(Collectors.toSet());

        // Hämta alla Food-objekt i en batch baserat på de foodNames som behövs
        List<Food> foods = foodRepo.findByFoodNameIn(foodNames);

        // Skapa en map för snabb åtkomst av Food-objekt baserat på foodName
        Map<String, Food> foodMap = foods.stream()
                .collect(Collectors.toMap(Food::getFoodName, food -> food));

        // Bearbeta varje order
        for (CustomerOrder customerOrder : customerOrders) {
            try {
                // Konvertera foodNames till riktiga Food-objekt
                List<Food> foodList = customerOrder.getFoodOrders().stream()
                        .map(food -> Optional.ofNullable(foodMap.get(food.getFoodName()))
                                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + food.getFoodName())))
                        .collect(Collectors.toList());

                customerOrder.setFoodOrders(foodList);

                // Beräkna totalpris för ordern
                double totalPrice = foodList.stream().mapToDouble(Food::getFoodPrice).sum();
                customerOrder.setOrderPrice(totalPrice);

                orderRepo.save(customerOrder);
            } catch (Exception e) {
                errors.add("Error with order " + customerOrder.getFoodOrders() + ": " + e.getMessage());
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
            List<Food> foodList = customerOrder.getFoodOrders().stream()
                    .map(food -> foodRepo.findFoodByFoodName(food.getFoodName())
                            .orElseThrow(() -> new IllegalArgumentException("Food not found: " + food.getFoodName())))
                    .collect(Collectors.toList());

            existingCustomerOrder.setFoodOrders(foodList);
            existingCustomerOrder.setOrderTable(customerOrder.getOrderTable());
            existingCustomerOrder.setOrderDateTime(customerOrder.getOrderDateTime());
            existingCustomerOrder.setOrderSpecial(customerOrder.getOrderSpecial());
            existingCustomerOrder.setOrderPrice(customerOrder.getOrderPrice());
            existingCustomerOrder.setOrderState(customerOrder.getOrderState());

            orderRepo.save(existingCustomerOrder);
            return "Order updated successfully!";
        } catch (Exception e) {
            return "Error updating order: " + e.getMessage();
        }
    }

    @PutMapping("/{orderTable}/addFood/{foodName}")
    public String addFoodToOrder(@PathVariable int orderTable, @PathVariable String foodName) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Food foodToAdd = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        // Kontrollera om maten redan finns i ordern
        if (existingCustomerOrder.getFoodOrders().contains(foodToAdd)) {
            return "Food " + foodName + " is already in this order.";
        }

        existingCustomerOrder.getFoodOrders().add(foodToAdd);

        double newOrderPrice = existingCustomerOrder.getFoodOrders().stream()
                .mapToDouble(Food::getFoodPrice)
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

        if (!existingCustomerOrder.getFoodOrders().contains(foodToRemove)) {
            return "Food " + foodName + " is not in this order.";
        }

        existingCustomerOrder.getFoodOrders().remove(foodToRemove);

        double newOrderPrice = existingCustomerOrder.getFoodOrders().stream()
                .mapToDouble(Food::getFoodPrice)
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
