package com.aeatirk.customer.domain.repository;

import com.aeatirk.customer.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
