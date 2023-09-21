package com.restaurant.orderingsystem.model.requests;

import com.restaurant.orderingsystem.model.OrderState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderStateRequest {

    @NotNull
    private OrderState orderState;
}
