package com.aeatirk.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDTO(Long id, LocalDateTime orderDate, BigDecimal totalAmount) {
}
