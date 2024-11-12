package com.aeatirk.customer.domain.repository;

import com.aeatirk.customer.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
