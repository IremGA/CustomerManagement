package com.aeatirk.customer.service;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    public CompletableFuture<Boolean> validateEmail(String email);
}
