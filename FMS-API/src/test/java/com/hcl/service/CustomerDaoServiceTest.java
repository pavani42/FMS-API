package com.hcl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.daoService.CustomerDaoService;
import com.hcl.dto.AccountDto;
import com.hcl.dto.CustomerDto;
import com.hcl.entity.*;
import com.hcl.repository.AccountRepo;
import com.hcl.repository.CustomerRepo;
import com.hcl.repository.TransactionRepo;
import org.apache.tomcat.util.digester.ArrayStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CustomerDaoServiceTest {
    @InjectMocks
    CustomerDaoService customerDaoService;

    @Mock
    CustomerRepo customerRepo;
    @Mock
    AccountRepo accountRepo;
    @Mock
    TransactionRepo transactionRepo;

    AccountDto accountDto;

    Customer customer;

    @BeforeEach
    public void setup()
    {
        customer = new Customer(1, "Pavani");
        accountDto = AccountDto.builder().id(1).customer(new Customer(1, "Pavani"))
                .balance(5.5).accType(AccountType.CA).actccy("INR").accNum(Long.parseLong("1234567891234567"))
                .build();
    }

    @Test
    public void testCustomerAccount()
    {
        Mockito.when(accountRepo.save(Mockito.any(Account.class))).thenReturn(null);
        assertEquals(HttpStatus.CREATED, customerDaoService.createAccount(new AccountDto()));
    }

    @Test
    public void testGetCustomer()
    {
        Optional<Customer> customer = Optional.of(new Customer(1,"Pavani"));
        Mockito.when(customerRepo.findById(Long.parseLong("1"))).thenReturn(customer);
        assertEquals(customer.get(), customerDaoService.getCustomer(1));
    }

    @Test
    public void testCreateCustomer()
    {
//        Mockito.when(customerRepo.save(new Customer(1, "Pavani")));
        Mockito.when(customerRepo.getByCust_Name("Pavani")).thenReturn(new Customer(1,"Pavani"));
        assertEquals(new Customer(1, "Pavani"), customerDaoService.createCustomer("Pavani"));
    }

    @Test
    public void addCustomerBalance()
    {
//        Optional<Customer> customerList = Optional.of(new Customer(1,"Pavani"));
//        Mockito.when(customerDaoService.getCustomer(1)).thenReturn(customerList.get());
        Optional<Customer> customer1 = Optional.of(new Customer(1,"Pavani"));
        Mockito.when(customerRepo.findById(Long.parseLong("1"))).thenReturn(customer1);
        Mockito.when(accountRepo.updateBalance(5.0, 1,new Customer(1,"Pavani") )).thenReturn(3);
        assertEquals(ReasonCodes.APPROVED, customerDaoService.addBalance(5.0, 1, 1));
    }

    @Test
    public void addCustomerBalanceThrowsExcept()
    {
//        Optional<Customer> customerList = Optional.of(new Customer(1,"Pavani"));
//        Mockito.when(customerDaoService.getCustomer(1)).thenReturn(customerList.get());
        Optional<Customer> customer1 = Optional.of(new Customer(1,"Pavani"));
        Mockito.when(customerRepo.findById(Long.parseLong("1"))).thenReturn(customer1);
        Mockito.when(accountRepo.updateBalance(5.0, 1,new Customer(1,"Pavani") )).thenReturn(0);
        assertEquals(ReasonCodes.MESSAGEFORMATERROR, customerDaoService.addBalance(5.0, 1, 1));
    }

    @Test
    public void testGetBalance() throws JsonProcessingException {
        Optional<Customer> customer1 = Optional.of(new Customer(1,"Pavani"));
        customer1.get().setAccountList(List.of(Account.builder().balance(5).id(1).actccy("INR").build()));
        Mockito.when(customerRepo.findById(Long.parseLong("1"))).thenReturn(customer1);
        Transaction_Audit transaction_Audit = Transaction_Audit.builder().id("ehfgd-hjgv")
                .messgInTime(LocalDateTime.now()).messOutTime(LocalDateTime.now()).status(StatusMessage.SUCCESS)
                .reasonCode(ReasonCodes.APPROVED).iMessage("success").build();
        Mockito.when(transactionRepo.save(Mockito.any(Transaction_Audit.class))).thenReturn(transaction_Audit);
        assertNotNull(customerDaoService.getBalance(1, LocalDateTime.now(), "/api/customer"));
    }

    @Test
    public void testTransactionAuditList()
    {
        Transaction_Audit transaction_Audit = Transaction_Audit.builder().id("ehfgd-hjgv")
                .messgInTime(LocalDateTime.now()).messOutTime(LocalDateTime.now()).status(StatusMessage.SUCCESS)
                .reasonCode(ReasonCodes.APPROVED).iMessage("success").build();
        List<Transaction_Audit> transactionAuditList = new ArrayList<>();
        transactionAuditList.add(transaction_Audit);
        Mockito.when(transactionRepo.findAll()).thenReturn(transactionAuditList);
//        Mockito.doNothing().when(Collections.sort(transactionAuditList);
//        Collections.sort(transactionAuditList, new Comparator<Transaction_Audit>() {
//            @Override
//            public int compare(Transaction_Audit o1, Transaction_Audit o2) {
//                return o1.getMessgInTime().compareTo(o2.getMessgInTime());
//            }
//        });
        assertEquals(1, customerDaoService.getTransactionAudit().size());
    }

}
