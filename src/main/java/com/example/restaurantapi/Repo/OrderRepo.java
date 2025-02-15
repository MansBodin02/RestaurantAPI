package com.example.restaurantapi.Repo;

import com.example.restaurantapi.Models.Order.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<CustomerOrder, Long> {
    CustomerOrder findOrderByOrderTable(int orderTable);
}
