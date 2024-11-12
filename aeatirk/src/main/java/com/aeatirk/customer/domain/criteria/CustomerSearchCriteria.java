package com.aeatirk.customer.domain.criteria;

import com.aeatirk.customer.domain.model.LoyaltyGrade;
import com.aeatirk.customer.domain.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSearchCriteria {

    private String name;
    private String surname;
    private String email;
    private Status status;
    private LoyaltyGrade loyaltyGrade;
}
