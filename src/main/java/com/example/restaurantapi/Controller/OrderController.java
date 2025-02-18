package com.example.restaurantapi.Controller;
import com.example.restaurantapi.Models.Drink.Drink;
import com.example.restaurantapi.Models.Food.Food;
import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Order.OrderItem;
import com.example.restaurantapi.Repo.FoodRepo;
import com.example.restaurantapi.Repo.DrinkRepo;
import com.example.restaurantapi.Repo.OrderRepo;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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
                Food food = null;
                Drink drink = null;

                if (item.getFood() != null && item.getFood().getFoodName() != null) {
                    food = foodRepo.findFoodByFoodName(item.getFood().getFoodName())
                            .orElseThrow(() -> new IllegalArgumentException("Food not found: " + item.getFood().getFoodName()));
                }

                if (item.getDrink() != null && item.getDrink().getDrinkName() != null) {
                    drink = drinkRepo.findDrinkByDrinkName(item.getDrink().getDrinkName())
                            .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + item.getDrink().getDrinkName()));
                }

                orderItems.add(new OrderItem(customerOrder, food, item.getFoodQuantity(),
                        drink, item.getDrinkQuantity(), item.getOrderItemSpecial()));
            }

            customerOrder.setOrderItems(orderItems);

            double totalPrice = orderItems.stream()
                    .mapToDouble(item -> (item.getFood() != null ? item.getFood().getFoodPrice() * item.getFoodQuantity() : 0) +
                            (item.getDrink() != null ? item.getDrink().getDrinkPrice() * item.getDrinkQuantity() : 0))
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

        for (CustomerOrder customerOrder : customerOrders) {
            try {
                List<OrderItem> orderItems = customerOrder.getOrderItems().stream()
                        .map(orderItem -> {
                            Food food = null;
                            Drink drink = null;

                            if (orderItem.getFood() != null && orderItem.getFood().getFoodName() != null) {
                                food = foodRepo.findFoodByFoodName(orderItem.getFood().getFoodName())
                                        .orElseThrow(() -> new IllegalArgumentException("Food not found: " + orderItem.getFood().getFoodName()));
                            }

                            if (orderItem.getDrink() != null && orderItem.getDrink().getDrinkName() != null) {
                                drink = drinkRepo.findDrinkByDrinkName(orderItem.getDrink().getDrinkName())
                                        .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + orderItem.getDrink().getDrinkName()));
                            }

                            return new OrderItem(customerOrder, food, orderItem.getFoodQuantity(),
                                    drink, orderItem.getDrinkQuantity(), orderItem.getOrderItemSpecial());
                        })
                        .collect(Collectors.toList());

                customerOrder.setOrderItems(orderItems);

                double totalPrice = orderItems.stream()
                        .mapToDouble(item -> (item.getFood() != null ? item.getFood().getFoodPrice() * item.getFoodQuantity() : 0) +
                                (item.getDrink() != null ? item.getDrink().getDrinkPrice() * item.getDrinkQuantity() : 0))
                        .sum();
                customerOrder.setOrderPrice(totalPrice);

                orderRepo.save(customerOrder);
            } catch (Exception e) {
                errors.add("Error with order: " + e.getMessage());
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
            List<OrderItem> orderItems = customerOrder.getOrderItems().stream()
                    .map(orderItem -> {
                        Food food = foodRepo.findFoodByFoodName(orderItem.getFood().getFoodName())
                                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + orderItem.getFood().getFoodName()));

                        Drink drink = drinkRepo.findDrinkByDrinkName(orderItem.getDrink().getDrinkName())
                                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + orderItem.getDrink().getDrinkName()));

                        return new OrderItem(existingCustomerOrder, food, orderItem.getFoodQuantity(), drink, orderItem.getDrinkQuantity(), orderItem.getOrderItemSpecial());
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


    //IN PROGRESS
    /**
    @PutMapping("/{orderTable}/addDrink/{drinkName}")
    public String addDrinkToOrder(@PathVariable int orderTable, @PathVariable String drinkName, @RequestParam String orderItemSpecial) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Drink drinkToAdd = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        boolean drinkExists = existingCustomerOrder.getOrderItems().stream()
                .anyMatch(orderItem -> orderItem.getDrink().equals(drinkToAdd));

        if (drinkExists) {
            return "Drink " + drinkName + " is already in this order.";
        }

        OrderItem orderItem = new OrderItem(existingCustomerOrder, null, drinkToAdd, 0, 1, orderItemSpecial);
        existingCustomerOrder.getOrderItems().add(orderItem);

        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> (item.getFood() != null ? item.getFood().getFoodPrice() * item.getFoodQuantity() : 0) +
                        (item.getDrink() != null ? item.getDrink().getDrinkPrice() * item.getDrinkQuantity() : 0))
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Drink " + drinkName + " added to order and order price updated to " + newOrderPrice;
    }


/**
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
        OrderItem orderItem = new OrderItem(existingCustomerOrder, foodToAdd, 1 , orderItemSpecial);  // 1 är en default mängd
        existingCustomerOrder.getOrderItems().add(orderItem);

        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> item.getFood().getFoodPrice() * item.getFoodQuantity())
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Food " + foodName + " added to order and order price updated to " + newOrderPrice;
    }
**/

    @PutMapping("/{orderTable}/removeFood/{foodName}")
    public String removeFoodFromOrder(@PathVariable int orderTable, @PathVariable String foodName) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Food foodToRemove = foodRepo.findFoodByFoodName(foodName)
                .orElseThrow(() -> new IllegalArgumentException("Food not found: " + foodName));

        OrderItem orderItemToRemove = existingCustomerOrder.getOrderItems().stream()
                .filter(orderItem -> orderItem.getFood() != null && orderItem.getFood().equals(foodToRemove))
                .findFirst()
                .orElse(null);

        if (orderItemToRemove == null) {
            return "Food " + foodName + " is not in this order.";
        }

        existingCustomerOrder.getOrderItems().remove(orderItemToRemove);

        // Beräkna nytt orderpris med både mat och dryck
        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> (item.getFood() != null ? item.getFood().getFoodPrice() * item.getFoodQuantity() : 0) +
                        (item.getDrink() != null ? item.getDrink().getDrinkPrice() * item.getDrinkQuantity() : 0))
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Food " + foodName + " removed from order and order price updated to " + newOrderPrice;
    }





    @PutMapping("/{orderTable}/removeDrink/{drinkName}")
    public String removeDrinkFromOrder(@PathVariable int orderTable, @PathVariable String drinkName) {
        CustomerOrder existingCustomerOrder = orderRepo.findOrderByOrderTable(orderTable);
        if (existingCustomerOrder == null) {
            return "Order not found at order table: " + orderTable;
        }

        Drink drinkToRemove = drinkRepo.findDrinkByDrinkName(drinkName)
                .orElseThrow(() -> new IllegalArgumentException("Drink not found: " + drinkName));

        OrderItem orderItemToRemove = existingCustomerOrder.getOrderItems().stream()
                .filter(orderItem -> orderItem.getDrink().equals(drinkToRemove))
                .findFirst()
                .orElse(null);

        if (orderItemToRemove == null) {
            return "Drink " + drinkName + " is not in this order.";
        }

        existingCustomerOrder.getOrderItems().remove(orderItemToRemove);

        double newOrderPrice = existingCustomerOrder.getOrderItems().stream()
                .mapToDouble(item -> (item.getFood() != null ? item.getFood().getFoodPrice() * item.getFoodQuantity() : 0) +
                        (item.getDrink() != null ? item.getDrink().getDrinkPrice() * item.getDrinkQuantity() : 0))
                .sum();

        existingCustomerOrder.setOrderPrice(newOrderPrice);

        orderRepo.save(existingCustomerOrder);
        return "Drink " + drinkName + " removed from order and order price updated to " + newOrderPrice;
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
