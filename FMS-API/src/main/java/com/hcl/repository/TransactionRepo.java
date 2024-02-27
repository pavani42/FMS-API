package com.hcl.repository;

import com.hcl.entity.Transaction_Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepo extends JpaRepository<Transaction_Audit, Long> {
}
