package com.prince.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prince.orderservice.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	
	
}
