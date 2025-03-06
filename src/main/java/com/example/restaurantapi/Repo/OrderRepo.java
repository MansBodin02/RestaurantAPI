package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Order.OrderCategory;
import com.example.restaurantapi.Models.Order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends JpaRepository<CustomerOrder, Long> {
    CustomerOrder findOrderByOrderId(Long orderId);
    CustomerOrder findOrderByOrderDateTime(LocalDateTime orderDateTime);
    List<CustomerOrder> findOrdersByOrderTable(int orderTable);
    List<CustomerOrder> findOrdersByOrderState(OrderState orderState);
    List<CustomerOrder> findCustomerOrdersByOrderCategory(OrderCategory orderCategory);
}
