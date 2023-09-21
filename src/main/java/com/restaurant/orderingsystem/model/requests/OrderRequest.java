package com.restaurant.orderingsystem.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @JsonProperty("orderItems")
    @NotNull(message = "Order items shouldn't be null")
    @NotEmpty(message = "Order items shouldn't be empty")
    private List<OrderItemsRequest> orderItems;

}


