package com.aeatirk.customer.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomerAPIException extends RuntimeException{
    private String message;
    private String apiName;
    private HttpStatus httpStatus;
    private long timestamp;
    private String errorMessage;

    public CustomerAPIException(String message, HttpStatus httpStatus, String applicationName) {

        this.message = message;
        this.httpStatus = httpStatus;
        this.apiName = applicationName;
        this.timestamp = System.currentTimeMillis();
    }
    public CustomerAPIException(String message, HttpStatus httpStatus, String applicationName, String errorMessage) {

        this.message = message;
        this.httpStatus = httpStatus;
        this.apiName = applicationName;
        this.timestamp = System.currentTimeMillis();
        this.errorMessage = errorMessage;
    }
}
