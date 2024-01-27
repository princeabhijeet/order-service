package com.prince.orderservice.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.prince.orderservice.dtos.OrderDto;
import com.prince.orderservice.dtos.OrderResponse;
import com.prince.orderservice.dtos.Payment;
import com.prince.orderservice.dtos.PaymentDto;
import com.prince.orderservice.dtos.Product;
import com.prince.orderservice.entities.Order;
import com.prince.orderservice.exception.OrderServiceCustomException;
import com.prince.orderservice.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Order placeOrder(OrderDto orderDto) {
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("productId", orderDto.getProductId().toString());
		inputMap.put("quantity", orderDto.getQuantity().toString());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
		
		ResponseEntity<Product> productResponse = restTemplate.exchange("http://PRODUCT-SERVICE/api/product/reduce-product-quantity/{productId}/{quantity}", HttpMethod.PUT, entity, Product.class, inputMap);
		Product product = productResponse.getBody();
		log.info("Product quantity reduced to {} for productId {}", product.getQuantity(), product.getProductId());
		
		Order order = Order.builder()
				.productId(orderDto.getProductId())
				.quantity(orderDto.getQuantity())
				.totalAmount(orderDto.getTotalAmount())
				.orderStatus("CREATED")
				.orderDate(Timestamp.from(Instant.now()))
				.build();
		Order savedOrder = orderRepository.save(order);
		log.info("OrderId {} successfully saved in database!", savedOrder.getOrderId());
		
		PaymentDto paymentDto = PaymentDto.builder()
				.orderId(savedOrder.getOrderId())
				.totalAmount(savedOrder.getTotalAmount())
				.build();
		
		String newOrderStatus = "";
		try {
			ResponseEntity<Payment> paymentResponse = restTemplate.postForEntity("http://PAYMENT-SERVICE/api/payment", paymentDto, Payment.class);
			Payment payment = paymentResponse.getBody();
			newOrderStatus = "PLACED";
			log.info("Payment done successfully with paymentId {}. Changing orderStatus to PLACED", payment.getPaymentId());
		}
		catch(Exception e) {
			e.printStackTrace();
			newOrderStatus = "PAYMENT_FAILED";
			log.error("Payment failed for orderId {}. Changing orderStatus to PAYMENT_FAILED", savedOrder.getOrderId());
		}
		savedOrder.setOrderStatus(newOrderStatus);
		savedOrder = orderRepository.save(order);
		return savedOrder;
	}

	@Override
	public OrderResponse getOrder(Long orderId) {
		
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if(!optionalOrder.isPresent()) {
			log.error("OrderId {} not found", orderId);
			throw new OrderServiceCustomException("OrderId " + orderId + " not found!", "ORDER_NOT_FOUND");
		}
		
		Order order = optionalOrder.get();
		log.info("OrderId {} found in database with current orderStatus {}", orderId, order.getOrderStatus());
		
		Map<String, String> inputMap = new HashMap<>();
		inputMap.put("productId", order.getProductId().toString());
		Product product = restTemplate.getForObject("http://PRODUCT-SERVICE/api/product/{productId}", Product.class, inputMap);
		
		if(null == product) {
			throw new OrderServiceCustomException("OrderId " + orderId + " does not have associated productId " + order.getProductId() + " details in database!", "ORDER_PRODUCT_NOT_FOUND");
		}
		
		inputMap.clear();
		inputMap.put("orderId", orderId.toString());
		Payment payment = restTemplate.getForObject("http://PAYMENT-SERVICE/api/payment/{orderId}", Payment.class, inputMap);
		
		if(null == payment) {
			throw new OrderServiceCustomException("OrderId " + orderId + " does not have associated paymentId", "ORDER_PAYMENT_NOT_FOUND");
		}
		
		OrderResponse orderResponse = OrderResponse.builder()
				.orderId(orderId)
				.productId(product.getProductId())
				.quantity(order.getQuantity())
				.totalAmount(order.getTotalAmount())
				.orderStatus(order.getOrderStatus())
				.orderDate(order.getOrderDate())
				.product(product)
				.payment(payment)
				.build();
		
		log.info("OrderId {} | productId {} | paymentId {} found in database", orderId, orderResponse.getProductId(), orderResponse.getPayment().getPaymentId());
		
		return orderResponse;
	}

}
