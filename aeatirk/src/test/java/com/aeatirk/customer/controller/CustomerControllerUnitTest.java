package com.aeatirk.customer.controller;


import com.aeatirk.customer.constant.Constants;
import com.aeatirk.customer.domain.model.Customer;
import com.aeatirk.customer.dto.CustomerDTO;
import com.aeatirk.customer.util.DTOMapper;
import com.aeatirk.customer.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerUnitTest {
    @Mock
    private CustomerService customerService;

    private MockedStatic<DTOMapper> mockedDTOMapper;

    @InjectMocks
    private CustomerController customerController;

    private Customer customer;
    private CustomerDTO customerDTO;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Irem");
        customer.setSurname("Aktas");

        customerDTO = new CustomerDTO(1L, "Irem", "Aktas", "iremgamzei@gmail.com", Constants.BRONZE);
        mockedDTOMapper = mockStatic(DTOMapper.class);
    }

    @AfterEach
    public void tearDown() {
        if (mockedDTOMapper != null) {
            mockedDTOMapper.close();
        }
    }

    @Test
    public void testGetCustomerById() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    public void testGetAllCustomers() {
        List<CustomerDTO> customers = Arrays.asList(customerDTO);
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList(customerDTO));

        ResponseEntity<List<CustomerDTO>> response = customerController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(customerDTO, response.getBody().get(0));
        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    public void testCreateCustomer() {
        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.createCustomer(customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
        verify(customerService, times(1)).createCustomer(any(CustomerDTO.class));
    }

    @Test
    public void testUpdateCustomer() {
        when(customerService.updateCustomer(anyLong(),any(CustomerDTO.class))).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.updateCustomer(1L, customerDTO);
        mockedDTOMapper.when(() -> DTOMapper.mapCustomerToCustomerDTO(any(Customer.class), anyString())).thenReturn(customerDTO);
        mockedDTOMapper.when(() -> DTOMapper.mapCustomerCTOToCustomer(any(CustomerDTO.class))).thenReturn(customer);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
        verify(customerService, times(1)).updateCustomer(anyLong(), any(CustomerDTO.class));
    }

    @Test
    public void testDeleteCustomer() {

        when(customerService.deleteCustomer(anyLong())).thenReturn(true);
        ResponseEntity<Void> response = customerController.deleteCustomer(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1)).deleteCustomer(1L);
    }
}
