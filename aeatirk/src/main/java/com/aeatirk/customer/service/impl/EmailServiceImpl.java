package com.aeatirk.customer.service.impl;

import com.aeatirk.customer.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {

    private final RestTemplate restTemplate;

    @Value("${emailValidationURL}")
    private String emailValidationURL;

    public EmailServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Retryable(
            value = { RestClientException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public CompletableFuture<Boolean> validateEmail(String email) {
        final String url = emailValidationURL + "?email=" +email;
        try{
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            boolean isValid = response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody();
            return  CompletableFuture.completedFuture(isValid);
        } catch(RestClientException e){
             throw e;
        }
    }

    @Recover
    public CompletableFuture<Boolean> recover(RestClientException e, String email) {
        // Log failure or take an alternative action
        System.out.println("Email validation failed after retries to the Email Validation API : " + email);
        return CompletableFuture.completedFuture(false); // Return a default value or handle the error
    }
}
