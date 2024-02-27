package com.hcl.daoService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hcl.dto.AccountDto;
import com.hcl.dto.CustomerDto;
import com.hcl.entity.*;
import com.hcl.exceptions.CustomException;
import com.hcl.repository.AccountRepo;
import com.hcl.repository.CustomerRepo;
import com.hcl.repository.TransactionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerDaoService {
    @Autowired
    AccountRepo accountRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    TransactionRepo transactionRepo;

    public HttpStatus createAccount(AccountDto account)
    {
        accountRepo.save(covertToAccount(account));
        return HttpStatus.CREATED;
    }

    private Account covertToAccount(AccountDto account) {
        return Account.builder().id(account.getId()).customer(account.getCustomer())
                .accType(account.getAccType()).accNum(account.getAccNum()).balance(account.getBalance())
                .actccy(account.getActccy()).build();
    }

    public Customer getCustomer(long customerId)
    {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if(customer.isEmpty())
            throw new CustomException(" " +Long.toString(customerId) + " " + ReasonCodes.INVALID.getStatusDesc());
        return customer.get();
    }

    public Customer createCustomer(String customerName) {
        customerRepo.save(new Customer(0, customerName));
        return customerRepo.getByCust_Name(customerName);
    }

    public ReasonCodes addBalance(double balance, long customerId, long accountNumber) {
        Customer customer = getCustomer(customerId);
        if(accountRepo.updateBalance(balance, accountNumber, customer) > 0)
            return ReasonCodes.APPROVED;
        else
            return ReasonCodes.MESSAGEFORMATERROR;
    }

    public CustomerDto getBalance(long customerId, LocalDateTime time, String urlPath) throws JsonProcessingException {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> {throw new CustomException(urlPath + " " + Long.toString(customerId) +  " " + ReasonCodes.INVALID.getStatusDesc());});
        List<Object> accountDtoList = parseToAccountDto(customer.getAccountList());
        Transaction_Audit transactionAudit = new Transaction_Audit();
        transactionAudit.setIMessage("http://localhost:8081/api/" + urlPath + customerId);
        transactionAudit.setMessgInTime(time);
        transactionAudit.setStatus(StatusMessage.SUCCESS);
        transactionAudit.setReasonCode(ReasonCodes.APPROVED);
        transactionAudit.setMessOutTime(LocalDateTime.now());
        saveTransaction(transactionAudit);
        return new CustomerDto(customer.getCust_Id(), customer.getCust_Name(), accountDtoList);
    }

    private List<Object> parseToAccountDto(List<Account> accountList) throws JsonProcessingException {
        List<Object> accountDtoList = new ArrayList<>();
        for(Account account: accountList) {
            accountDtoList.add(parseToJson(account));
        }
        return accountDtoList;
    }

    private Object parseToJson(Account account) throws JsonProcessingException {
        AccountDto accountdto = convertAccountDto(account);
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.setFailOnUnknownId(false).addFilter("AccountFilter", SimpleBeanPropertyFilter.filterOutAllExcept("accNum", "balance", "actccy"));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(filterProvider);
        Object accountDto = mapper.readValue(writer.writeValueAsString(accountdto), Object.class);
        return accountDto;
    }

    private AccountDto convertAccountDto(Account account) {
        System.out.println(account.getBalance());
        return AccountDto.builder().id(account.getId()).customer(account.getCustomer()).accNum(account.getAccNum())
                .actccy(account.getActccy()).accType(account.getAccType()).balance(account.getBalance()).build();
    }

    public void saveTransaction(Transaction_Audit transactionAudit)
    {
        System.out.println("coming here");
        transactionRepo.save(transactionAudit);
    }

    public List<Transaction_Audit> getTransactionAudit() {
        List<Transaction_Audit> transactionAuditList = transactionRepo.findAll();
        Collections.sort(transactionAuditList, new Comparator<Transaction_Audit>() {
            @Override
            public int compare(Transaction_Audit o1, Transaction_Audit o2) {
                return o1.getMessgInTime().compareTo(o2.getMessgInTime());
            }
        });
       return transactionAuditList;
    }


}
