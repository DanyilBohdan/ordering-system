package com.restaurant.orderingsystem.repositories;

import com.restaurant.orderingsystem.model.Order;
import com.restaurant.orderingsystem.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItem, Long> {
}
