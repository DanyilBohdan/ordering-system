package com.restaurant.orderingsystem.services;

import com.restaurant.orderingsystem.exceptions.ConflictIllegalStateException;
import com.restaurant.orderingsystem.exceptions.NoOrderException;
import com.restaurant.orderingsystem.model.*;
import com.restaurant.orderingsystem.model.requests.OrderRequest;
import com.restaurant.orderingsystem.repositories.OrderItemsRepository;
import com.restaurant.orderingsystem.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.restaurant.orderingsystem.model.OrderState.*;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemsRepository orderItemsRepository;
    @Autowired
    private ProductService productService;

    @Override
    @Transactional
    public Long createOrder(OrderRequest orderRequest) {

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderItemsRequest -> {
                    Product product = productService.getProductById(orderItemsRequest.getProductId());
                    BigDecimal totalPriceOfProduct = product.getPrice().multiply(BigDecimal.valueOf(orderItemsRequest.getAmount()));
                    return OrderItem.builder()
                            .amount(orderItemsRequest.getAmount())
                            .product(product)
                            .totalPriceOfItem(totalPriceOfProduct)
                            .additionalVariable(orderItemsRequest.getAdditionalVariables())
                            .build();
                })
                .toList();

        Order newOrder = Order.builder()
                .orderItems(orderItems)
                .orderState(IN_LINE)
                .totalPrice(calculateTotalPrice(orderItems))
                .build();

        orderItemsRepository.saveAll(orderItems);
        return orderRepository.save(newOrder).getId();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getTotalPriceOfItem)
                .reduce(BigDecimal::add).get();
    }

    @Override
    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public List<Order> getAllOrders(OrderState orderState) {
        return getAllOrders().stream()
                .filter(order -> order.getOrderState().equals(orderState))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new NoOrderException("Not found order with id = " + id));
    }

    @Override
    @Transactional
    public Order updateOrderState(Long id, OrderState newState) {
        final Order order = getOrderById(id);
        OrderState currentState = order.getOrderState();
        if (currentState == null)
            throw new ConflictIllegalStateException("State for " + id + " can not be found.");
        if (currentState.equals(newState)) {
            throw new ConflictIllegalStateException(id + " already at " + newState);
        }

        switch (currentState) {
            default ->
                    throw new ConflictIllegalStateException(id + " can not be set to " + newState + " from " + currentState);
            case IN_LINE -> {
                if (newState.equals(IN_PROGRESS)) {
                    order.setOrderState(newState);
                } else {
                    throw new ConflictIllegalStateException(id + " can not be set to " + newState + " from " + currentState);
                }
            }
            case IN_PROGRESS -> {
                if (newState.equals(COMPLETED)) {
                    order.setOrderState(newState);
                } else {
                    throw new ConflictIllegalStateException(id + " can not be set to " + newState + " from " + currentState);
                }
            }
            case COMPLETED -> throw new ConflictIllegalStateException(id + " can not be updated. Already DONE");
        }

        return orderRepository.save(order);
    }
}
