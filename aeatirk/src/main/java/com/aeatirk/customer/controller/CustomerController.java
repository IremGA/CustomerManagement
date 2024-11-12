package com.aeatirk.customer.controller;

import com.aeatirk.customer.domain.criteria.CustomerSearchCriteria;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.LoyaltyGrade;
import com.aeatirk.customer.dto.CustomerDTO;
import com.aeatirk.customer.exception.CustomerAPIExceptionResponse;
import com.aeatirk.customer.service.CustomerService;
import com.aeatirk.customer.util.DTOMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController{

    @Value("${spring.application.name}")
    private String applicationName;
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomerAPIExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        CustomerAPIExceptionResponse errorResponse =
                new CustomerAPIExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, applicationName);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<CustomerDTO>> searchCustomers(
            @RequestBody CustomerSearchCriteria criteria,
            Pageable pageable) {

        Page<CustomerDTO> customers = customerService.searchCustomers(criteria, pageable);

        return ResponseEntity.ok(customers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        if (customer != null) {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PostMapping("/create-customer")
    public ResponseEntity<CustomerDTO>  createCustomer( @RequestBody CustomerDTO customerDTO) {
        CustomerDTO customerDTO_res =  customerService.createCustomer(customerDTO);
        return ResponseEntity.ok(customerDTO_res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customerDTO) {
        CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
        if (updatedCustomer != null) {
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean isDeleted = customerService.deleteCustomer(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
