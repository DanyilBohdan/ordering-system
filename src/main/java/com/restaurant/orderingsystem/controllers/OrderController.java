package com.restaurant.orderingsystem.controllers;

import com.restaurant.orderingsystem.convertor.OrderConvertor;
import com.restaurant.orderingsystem.model.Order;
import com.restaurant.orderingsystem.model.OrderState;
import com.restaurant.orderingsystem.model.requests.OrderRequest;
import com.restaurant.orderingsystem.model.requests.OrderStateRequest;
import com.restaurant.orderingsystem.model.responses.OrderResponse;
import com.restaurant.orderingsystem.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Creates order
     *
     * @param orderRequest - order json request
     *                     example: {
     *     "orderItems": [
     *         {
     *             "productId": 1,
     *             "additionalVariables": {
     *                 "isIceCubes": true,
     *                 "lemon": false
     *             },
     *             "amount": 3
     *         }, {
     *             "productId": 2,
     *             "additionalVariables": {
     *                 "mainCourseId": 3,
     *                 "dessertId": 4
     *             },
     *             "amount": 5
     *         }
     *     ]
     * }
     * @return id of created order
     */
    @PostMapping("/order")
    public ResponseEntity<Long> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Long id = orderService.createOrder(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    /**
     * Returns all orders by order state
     * @param orderState - order state
     * @return order response json
     */
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@RequestParam(required = false) OrderState orderState) {

        List<Order> orders = orderState == null
                ? orderService.getAllOrders()
                : orderService.getAllOrders(orderState);

        return ResponseEntity.ok(OrderConvertor.convertOrderSetToOrderResponseSet(orders));
    }

    /**
     * Returns Order json by id
     * @param id - order id
     * @return order response json
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(OrderConvertor.convertOrderToOrderResponse(orderService.getOrderById(id)));
    }

    @PostMapping ("/order/{id}/state")
    public ResponseEntity<OrderResponse> updateOrderState(@PathVariable Long id, @Valid @RequestBody OrderStateRequest orderStateRequest) {
        return ResponseEntity.ok(OrderConvertor.convertOrderToOrderResponse(orderService.updateOrderState(id, orderStateRequest.getOrderState())));
    }
}
