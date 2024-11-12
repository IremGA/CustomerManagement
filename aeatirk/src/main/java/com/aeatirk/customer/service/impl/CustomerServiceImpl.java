package com.aeatirk.customer.service.impl;

import com.aeatirk.customer.domain.criteria.CustomerSearchCriteria;
import com.aeatirk.customer.domain.model.Account;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.LoyaltyGrade;
import com.aeatirk.customer.domain.model.RoyalCustomer;
import com.aeatirk.customer.domain.model.Status;
import com.aeatirk.customer.domain.repository.CustomerRepository;
import com.aeatirk.customer.domain.specification.CustomerSearchSpecification;
import com.aeatirk.customer.dto.CustomerDTO;
import com.aeatirk.customer.exception.CustomerAPIException;
import com.aeatirk.customer.service.CustomerService;
import com.aeatirk.customer.service.EmailService;
import com.aeatirk.customer.util.DTOMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Value("${spring.application.name}")
    private String applicationName;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    public CustomerServiceImpl(CustomerRepository customerRepository, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.emailService = emailService;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO){

        CompletableFuture<Boolean> emailValidationFuture = emailService.validateEmail(customerDTO.email());
        try{
            boolean isEmailValid = emailValidationFuture.get();
            if(!isEmailValid){
                throw new CustomerAPIException("Invalid E-mail, Customer Creation aborted!", HttpStatus.BAD_REQUEST,applicationName);
            }
            Customer customer = DTOMapper.mapCustomerCTOToCustomer(customerDTO);
            customer.setStatus(Status.ACTIVE);
            Account account = new Account();
            account.setLoyaltyPoints(0);
            customer.setAccount(account);
            customer = saveCustomer(customer);
            return DTOMapper.mapCustomerToCustomerDTO(customer, LoyaltyGrade.BRONZE.name());
        }catch(InterruptedException | ExecutionException e){
            Thread.currentThread().interrupt();
            throw new CustomerAPIException("Error occurred while calling email validation API", HttpStatus.INTERNAL_SERVER_ERROR,applicationName,e.getMessage());
        }catch(Exception e){
            throw new CustomerAPIException("Error occurred while creating Customer", HttpStatus.INTERNAL_SERVER_ERROR,applicationName,e.getMessage());

        }
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setName(customerDTO.name());
                    existingCustomer.setSurname(customerDTO.surname());
                    existingCustomer.setEmail(customerDTO.email());
                    Customer updatedCustomer = saveCustomer(existingCustomer);
                    return DTOMapper.mapCustomerToCustomerDTO(updatedCustomer, updatedCustomer.getAccount().getLoyaltyGrade().name());
                })
                .orElseThrow(() -> new CustomerAPIException("Customer not found", HttpStatus.NOT_FOUND, applicationName));
    }

    @Override
    public boolean deleteCustomer(Long id) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    existingCustomer.setStatus(Status.INACTIVE);
                    saveCustomer(existingCustomer);
                    return true;
                })
                .orElseThrow(() -> new CustomerAPIException("Customer not found", HttpStatus.NOT_FOUND, applicationName));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> DTOMapper.mapCustomerToCustomerDTO(customer, customer.getAccount().getLoyaltyGrade().name()))
                .orElseThrow(() -> new CustomerAPIException("Customer not found", HttpStatus.NOT_FOUND, applicationName));

    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOs = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOs.add(DTOMapper.mapCustomerToCustomerDTO(customer, customer.getAccount().getLoyaltyGrade().name()));
        }
        return customerDTOs;
    }

    @Transactional
    protected Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Page<CustomerDTO> searchCustomers(CustomerSearchCriteria criteria, Pageable pageable) {
        Specification<Customer> specification = new CustomerSearchSpecification(criteria);
        Page<Customer> customers = customerRepository.findAll(specification, pageable);
        return customers.map(CustomerDTO::new);
    }

}
