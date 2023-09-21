package com.restaurant.orderingsystem.convertor;

import com.restaurant.orderingsystem.model.Order;
import com.restaurant.orderingsystem.model.OrderItem;
import com.restaurant.orderingsystem.model.responses.OrderItemsResponse;
import com.restaurant.orderingsystem.model.responses.OrderResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderConvertor {

    public static OrderResponse convertOrderToOrderResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .orderItems(convertOrderItemSetToOrderItemResponseSet(order.getOrderItems()))
                .totalPrice(order.getTotalPrice())
                .orderState(order.getOrderState())
                .build();
    }

    public static List<OrderResponse> convertOrderSetToOrderResponseSet(List<Order> order) {
        return order.stream().map(OrderConvertor::convertOrderToOrderResponse).collect(Collectors.toList());
    }

    public static OrderItemsResponse convertOrderItemToOrderItemResponse(OrderItem orderItem) {
        return OrderItemsResponse.builder()
                .productId(orderItem.getProduct().getId())
                .amount(orderItem.getAmount())
                .additionalVariables(orderItem.getAdditionalVariable())
                .build();
    }

    public static List<OrderItemsResponse> convertOrderItemSetToOrderItemResponseSet(List<OrderItem> order) {
        return order.stream().map(OrderConvertor::convertOrderItemToOrderItemResponse).collect(Collectors.toList());
    }
}
