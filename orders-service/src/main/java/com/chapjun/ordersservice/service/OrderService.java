package com.chapjun.ordersservice.service;

import com.chapjun.ordersservice.dto.OrderDto;
import com.chapjun.ordersservice.entity.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);

}
