package com.prince.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prince.orderservice.dtos.OrderDto;
import com.prince.orderservice.dtos.OrderResponse;
import com.prince.orderservice.entities.Order;
import com.prince.orderservice.service.OrderService;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/order")
	private ResponseEntity<Order> createOrder(@RequestBody OrderDto orderDto) {
		Order order = orderService.placeOrder(orderDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
		OrderResponse orderResponse = orderService.getOrder(orderId);
		return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
	}

}
