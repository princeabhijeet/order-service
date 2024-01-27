package com.prince.orderservice.service;

import com.prince.orderservice.dtos.OrderDto;
import com.prince.orderservice.dtos.OrderResponse;
import com.prince.orderservice.entities.Order;

public interface OrderService {
	
	Order placeOrder(OrderDto orderDto);
	OrderResponse getOrder(Long orderId);

}
