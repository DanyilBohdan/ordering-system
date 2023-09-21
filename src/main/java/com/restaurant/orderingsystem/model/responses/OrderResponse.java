package com.restaurant.orderingsystem.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.orderingsystem.model.OrderItem;
import com.restaurant.orderingsystem.model.OrderState;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("totalPrice")
    private BigDecimal totalPrice;

    @JsonProperty("orderState")
    private OrderState orderState;

    @JsonProperty("orderItems")
    private List<OrderItemsResponse> orderItems;
}
