package com.prince.orderservice.dtos;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentDto {
	private Long orderId;
	private Long totalAmount;
	private String paymentStatus;
	private Timestamp paymentDate;
}
