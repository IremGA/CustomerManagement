package com.aeatirk.customer.domain.repository;

import com.aeatirk.customer.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
