package com.restaurant.orderingsystem.services;

import com.restaurant.orderingsystem.model.Order;
import com.restaurant.orderingsystem.model.OrderState;
import com.restaurant.orderingsystem.model.requests.OrderRequest;

import java.util.List;
import java.util.Set;

public interface OrderService {

    Long createOrder(OrderRequest orderRequest);

    List<Order> getAllOrders();

    List<Order> getAllOrders(OrderState orderState);

    Order getOrderById(Long id);

    Order updateOrderState(Long id, OrderState newState);

}
