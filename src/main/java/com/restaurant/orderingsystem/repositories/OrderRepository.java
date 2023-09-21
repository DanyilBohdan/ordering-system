package com.restaurant.orderingsystem.repositories;

import com.restaurant.orderingsystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
