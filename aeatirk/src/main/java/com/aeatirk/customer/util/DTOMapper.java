package com.aeatirk.customer.util;

import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.domain.model.RoyalCustomer;
import com.aeatirk.customer.dto.CustomerDTO;

public class DTOMapper {

    public static CustomerDTO mapCustomerToCustomerDTO(Object mappedObject, String grade){
        if(mappedObject == null)
        {
            throw new IllegalStateException("Mapped object is Null !!");
        }

        CustomerDTO customerDTO;
        switch (mappedObject){
            case RoyalCustomer royalCustomer ->{
                customerDTO = new CustomerDTO(royalCustomer.id(), royalCustomer.name(), royalCustomer.surname(), royalCustomer.email(), grade);
            }
            case Customer customer ->{
                customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getSurname(), customer.getEmail(), grade);
            }
            default -> throw new IllegalStateException("Unsupported Customer Type: " + mappedObject.getClass().getSimpleName());
        }
        return customerDTO;
    }

    public static Customer mapCustomerCTOToCustomer(CustomerDTO customerDTO){

        return Customer.builder()
                .name(customerDTO.name())
                .email(customerDTO.email())
                .surname(customerDTO.surname())
                .build();
    }
}
