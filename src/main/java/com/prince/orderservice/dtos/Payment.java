package com.prince.orderservice.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Payment {
	
	private Long paymentId;
	private Long orderId;
	private String referenceNumber;
	private Long totalAmount;
	private String paymentStatus;
	private Timestamp paymentDate;

}
