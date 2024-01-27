package com.prince.orderservice.dtos;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderResponse {
	private Long orderId;
	private Long productId;
	private Long quantity;
	private Long totalAmount;
	private String orderStatus;
	private Timestamp orderDate;
	private Product product;
	private Payment payment;
}
