package com.hcl.repository;

import com.hcl.entity.Account;
import com.hcl.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

//    @Query("select account.balance from Account accou")
    @Modifying
    @Transactional
    @Query("update Account account set account.balance = account.balance + ?1 where account.accNum = ?2 and account.customer = ?3")
    public int updateBalance(double balance, long accNum, Customer customer);

//    @Query("select account from Account account where account.customer.cust_Id = ?1")
//    public List

}
