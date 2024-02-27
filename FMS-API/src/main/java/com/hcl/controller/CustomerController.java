package com.hcl.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.dto.AccountDto;
import com.hcl.dto.CustomerDto;
import com.hcl.entity.Account;
import com.hcl.entity.ReasonCodes;
import com.hcl.entity.Transaction_Audit;
import com.hcl.exceptions.CustomException;
import com.hcl.service.CustomerService;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.net.InetSocketAddress;
import java.util.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/createAccount")
    public ReasonCodes createAccount(@RequestParam long customerId, @RequestParam String customerName, @RequestParam String accountType, @RequestParam String currency)
    {
       return customerService.CreateAccount(customerId, customerName, accountType, currency);
    }

    @PutMapping("/addBalance")
    public ReasonCodes addBalance(@RequestParam double balance, @RequestParam long customerId, @RequestParam long accountNumber)
    {
        return customerService.addBalnce(balance, customerId, accountNumber);
    }

    @GetMapping("/core/customer")
    public CustomerDto getBalance(@RequestParam long customerId) throws JsonProcessingException {
        LocalDateTime time = LocalDateTime.now();
        return customerService.getBalance(customerId, time, "/core/customer?customerId=");
    }

    @GetMapping("/core/customer/{customer-id}")
    public CustomerDto getAccountInfo(@PathVariable("customer-id") long customerId) throws JsonProcessingException {
        LocalDateTime time = LocalDateTime.now();
        return customerService.getBalance(customerId, time, "/core/customer/");
    }

    @GetMapping("/getTransactionAudit")
    public List<Transaction_Audit> getTransactionAudit()
    {
        return customerService.getTransactionAudit();
    }
}
