package com.prince.orderservice.exception;

public class OrderServiceCustomException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private String errorCode;
	
	public OrderServiceCustomException(String message, String errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
