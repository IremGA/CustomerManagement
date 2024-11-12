package com.aeatirk.customer.controller;
import com.aeatirk.customer.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Assuming your application main class is CustomerApplication
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    private MockWebServer mockWebServer;

    private static final String EMAIL_VALIDATION_URL = "http://email-manager-service/validate-email?email=";


    @BeforeEach
    void setUp() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(8080);

        Dispatcher dispatcher = new Dispatcher() {

            @Override
            public MockResponse dispatch(RecordedRequest request) {
                MockResponse result;
                String expectedValidEmailPath = "/validate-email?email=ie1234@gamil.com";

                // Check if the request path matches the expected path for the valid email
                if (request.getPath().equals(expectedValidEmailPath)) {
                    result = new MockResponse()
                            .setResponseCode(HttpStatus.OK.value())
                            .setBody("true")  // Respond with true for the valid email
                            .setHeader("Content-Type", "application/json");// Respond with true for the valid email
                } else {
                    result = new MockResponse()
                            .setResponseCode(HttpStatus.OK.value())
                            .setBody("false") // Respond with false for any other email
                            .setHeader("Content-Type", "application/json");// Respond with false for any other email
                }
                return result;
            }
        };

        // Assign the dispatcher to MockWebServer
        mockWebServer.setDispatcher(dispatcher);

    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    private String getRootUrl() {
        return "http://localhost:" + port + "/api/customers";
    }

    @Test
    @Order(3)
    public void testGetAllCustomers() {
        ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl(), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(2)
    public void testGetCustomerById() {
        int id = 1; // Set this to an ID that exists in the database
        ResponseEntity<String> response = restTemplate.getForEntity(getRootUrl() + "/" + id, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(1)
    public void testCreateCustomer() throws Exception {
        CustomerDTO customer = new CustomerDTO(null, "Irem", "Aktas", "ie1234@gamil.com", null); // Assuming you have a Customer class

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CustomerDTO> entity = new HttpEntity<>(customer, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(getRootUrl()+ "/create-customer", entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(4)
    public void testUpdateCustomer() throws Exception {
        int id = 1; // Set this to an ID that exists in the database
        CustomerDTO customer = new CustomerDTO(null, "Irem", "Aktas", "ie123@gmail.com", null); // Assuming you have a Customer class


        HttpHeaders headers = new HttpHeaders();
        HttpEntity<CustomerDTO> entity = new HttpEntity<>(customer, headers);

        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/" + id, HttpMethod.PUT, entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    public void testDeleteCustomer() {
        int id = 1; // Set this to an ID that exists in the database
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/" + id, HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
