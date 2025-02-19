package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import com.example.restaurantapi.Models.Order.OrderState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findOrdersByOrderTable(int orderTable);
    List<CustomerOrder> findOrdersByOrderState(OrderState orderState);

}
