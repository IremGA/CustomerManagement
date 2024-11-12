package com.aeatirk.customer.domain.specification;

import com.aeatirk.customer.domain.criteria.CustomerSearchCriteria;
import com.aeatirk.customer.domain.model.Account;
import com.aeatirk.customer.domain.model.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSearchSpecification implements Specification<Customer> {

    private final CustomerSearchCriteria customerSearchCriteria;

    public CustomerSearchSpecification(CustomerSearchCriteria customerSearchCriteria) {
        this.customerSearchCriteria = customerSearchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (customerSearchCriteria.getName() != null && !customerSearchCriteria.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + customerSearchCriteria.getName().toLowerCase() + "%"));
        }

        if (customerSearchCriteria.getSurname() != null && !customerSearchCriteria.getSurname().isEmpty()) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + customerSearchCriteria.getName().toLowerCase() + "%"));
        }


        if (customerSearchCriteria.getEmail() != null && !customerSearchCriteria.getEmail().isEmpty()) {
            predicates.add(criteriaBuilder.equal(root.get("email"), customerSearchCriteria.getEmail()));
        }

        if (customerSearchCriteria.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), customerSearchCriteria.getStatus()));
        }
        if (customerSearchCriteria.getLoyaltyGrade() != null) {
            Join<Customer, Account> accountJoin = root.join("account", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(accountJoin.get("loyaltyPoints"), customerSearchCriteria.getLoyaltyGrade()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
