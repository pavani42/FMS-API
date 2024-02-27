package com.hcl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.daoService.CustomerDaoService;
import com.hcl.dto.AccountDto;
import com.hcl.entity.AccountType;
import com.hcl.entity.Customer;
import com.hcl.entity.ReasonCodes;
import com.hcl.entity.Transaction_Audit;
import com.hcl.exceptions.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.hcl.dto.CustomerDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class CustomerService {

    Logger logger
            = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    CustomerDaoService customerDaoService;

    public ReasonCodes CreateAccount(long customerId, String customerName, String accountType, String currency)
    {
        logger.info("Creating Account....");
        AccountDto account = AccountDto.builder().build();
        if(customerId == 0)
           account.setCustomer(new Customer(customerId,customerName));
        else
            account.setCustomer(customerDaoService.getCustomer(customerId));
        account.setBalance(0.0);
        account.setActccy(currency);
        account.setAccNum(generateAccountNumber());
        if(accountType.equals("SBA"))
            account.setAccType(AccountType.SBA);
        else if(accountType.equals("CA"))
            account.setAccType(AccountType.CA);
        else if(accountType.equals("ODA"))
            account.setAccType(AccountType.ODA);
        else
            throw new CustomException(" " + Long.toString(customerId) + " " + ReasonCodes.MESSAGEFORMATERROR.getStatusDesc());
        customerDaoService.createAccount(account);
        return ReasonCodes.APPROVED;
    }

    private long generateAccountNumber() {
        long number = 0;
        for(int i = 0; i < 16; i++)
        {
            Random random = new Random();
            int randomWithNextInt = random.nextInt(0, 9);
            number = number * 10 + randomWithNextInt;
        }
        return number;
    }
    

    public ReasonCodes addBalnce(double balance, long customerId, long accountNumber) {
        if(Long.toString(accountNumber).length() != 16)
            throw new CustomException(" " +Long.toString(customerId)  + " " + ReasonCodes.INVALID.getStatusDesc());
        return customerDaoService.addBalance(balance, customerId, accountNumber);
    }

    public CustomerDto getBalance(long customerId, LocalDateTime time, String urlPath) throws JsonProcessingException {
        return customerDaoService.getBalance(customerId, time, urlPath);
    }
    public void saveTransaction(Transaction_Audit transactionAudit)
    {
        customerDaoService.saveTransaction(transactionAudit);
    }

    public List<Transaction_Audit> getTransactionAudit() {
        return customerDaoService.getTransactionAudit();
    }


}
