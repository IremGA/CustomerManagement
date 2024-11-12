package com.aeatirk.customer.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomerAPIExceptionResponse {

    private String message;
    private String apiName;
    private HttpStatus httpStatus;
    private long timestamp;

    public CustomerAPIExceptionResponse(String message, HttpStatus httpStatus, String applicationName) {

        this.message = message;
        this.httpStatus = httpStatus;
        this.apiName = applicationName;
        this.timestamp = System.currentTimeMillis();
    }

}
