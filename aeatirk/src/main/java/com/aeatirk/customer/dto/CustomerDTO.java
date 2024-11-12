package com.aeatirk.customer.dto;

import com.aeatirk.customer.constant.Constants;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.LoyaltyGrade;
import com.aeatirk.customer.util.LoyaltyGradeMapper;

public record CustomerDTO(Long id, String name, String surname, String email, String grade) {
    public CustomerDTO(Customer customer) {
        this(customer.getId(),
                customer.getName(),
                customer.getSurname(),
                customer.getEmail(),
                customer.getAccount() != null ? LoyaltyGradeMapper.calculateLoyaltyGrade(customer.getAccount().getLoyaltyPoints()): Constants.BRONZE);
    }


}
