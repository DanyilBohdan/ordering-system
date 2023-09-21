package com.restaurant.orderingsystem.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemsRequest {

    @JsonProperty("productId")
    @NotNull(message = "Product id is required")
    private Long productId;

    @JsonProperty("additionalVariables")
    private Map<String, String> additionalVariables;

    @JsonProperty("amount")
    @NotNull(message = "Product amount id is required")
    private int amount = 1;
}
