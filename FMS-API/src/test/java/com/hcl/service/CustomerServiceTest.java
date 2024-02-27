package com.hcl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.daoService.CustomerDaoService;
import com.hcl.entity.*;
import com.hcl.exceptions.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerDaoService customerDaoService;

//    @Mock
//    AccountRepo accountRepo;
//
//    @Mock
//    CustomerRepo customerRepo;
//
//    @Mock
//    TransactionRepo transactionRepo;

    @Test
    public void testCreateAccountForSBA()
    {
//        Mockito.when(accountRepo.save(Mockito.any(Account.class))).thenReturn(null);
        assertEquals(ReasonCodes.APPROVED, customerService.CreateAccount(1, "bindu", "SBA", "AUD"));
    }

    @Test
    public void testCreateAccountForCA()
    {
        assertEquals(ReasonCodes.APPROVED, customerService.CreateAccount(1, "bindu", "CA", "AUD"));
    }

    @Test
    public void testCreateAccountForODA()
    {
        assertEquals(ReasonCodes.APPROVED, customerService.CreateAccount(1, "bindu", "ODA", "AUD"));
    }

    @Test
    public void testCreateAccountException()
    {
        assertThrows(CustomException.class, () -> {
            customerService.CreateAccount(1, "bindu", "ODAs", "AUD");
        });
    }

    @Test
    public void testAddBalance()
    {
        Mockito.when(customerDaoService.addBalance(5.0, 1, Long.parseLong("5344234567891456"))).thenReturn(ReasonCodes.APPROVED);
//        Mockito.when(customerDaoService.getCustomer(1)).thenReturn(new Customer(53, "Pavani"));
//        Mockito.when(accountRepo.updateBalance(5.0, Long.parseLong("5344234567891456"), Mockito.any(Customer.class))).thenReturn(4);
        assertEquals(ReasonCodes.APPROVED, customerService.addBalnce(5.0, 1, Long.parseLong("5344234567891456")));
    }

    @Test
    public void testAddBalanceThrows()
    {
        assertThrows(CustomException.class, () ->{
            customerService.addBalnce(5.0, 1, Long.parseLong("53442345678914"));
        });
    }

    @Test
    public void testaddBalance() throws JsonProcessingException {
        Mockito.when(customerDaoService.addBalance(1, 1,Long.parseLong("5344234567891456"))).thenReturn(ReasonCodes.APPROVED);
        assertEquals(ReasonCodes.APPROVED, customerService.addBalnce(1, 1, Long.parseLong("5344234567891456")));
    }

    @Test
    public void testgetBalance() throws JsonProcessingException {
//        Mockito.when(customerDaoService.getBalance(1, LocalDateTime.now(),Mockito.any(String.class))).thenReturn(new CustomerDto(1, "Pavani", null));
        assertNull(customerService.getBalance(1,LocalDateTime.now(),"/api/customer"));
    }

    @Test
    public void testTransactionAudit()
    {
        Transaction_Audit transactionAudit = Transaction_Audit.builder().id("hkgfshkg").iMessage("APPROVED").messgInTime(LocalDateTime.now()).messOutTime(LocalDateTime.now())
                .reasonCode(ReasonCodes.APPROVED).status(StatusMessage.SUCCESS).build();
        List<Transaction_Audit> transactionAuditList = new ArrayList<>();
        transactionAuditList.add(transactionAudit);
        Mockito.when(customerDaoService.getTransactionAudit()).thenReturn(transactionAuditList);
        assertEquals(transactionAuditList, customerService.getTransactionAudit());
    }



}
