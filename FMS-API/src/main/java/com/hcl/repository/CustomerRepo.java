package com.hcl.repository;

import com.hcl.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    @Query("select customer from Customer customer where customer.cust_Name = ?1")
    Customer getByCust_Name(String customerName);
}
