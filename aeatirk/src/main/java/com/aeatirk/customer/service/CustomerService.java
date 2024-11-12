package com.aeatirk.customer.service;

import com.aeatirk.customer.domain.criteria.CustomerSearchCriteria;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.LoyaltyGrade;
import com.aeatirk.customer.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO);

    boolean deleteCustomer(Long id);

    CustomerDTO getCustomerById(Long id);

    List<CustomerDTO> getAllCustomers();
}
