package com.prince.orderservice.dtos;

import lombok.Data;

@Data
public class OrderDto {
	private Long productId;
	private Long quantity;
	private Long totalAmount;
	
}
