package com.aeatirk.customer.domain.repository;

import com.aeatirk.customer.constant.Constants;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.RoyalCustomer;
import com.aeatirk.customer.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Set<Customer> findByStatus(Status status);
    @Query(Constants.SEARCH_QUERY)
    @Transactional(readOnly = true)
    Set<RoyalCustomer> findByLoyaltyGrade(@Param("grade") String grade);

}
