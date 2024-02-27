package com.hcl.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcl.dto.CustomerDto;
import com.hcl.entity.ReasonCodes;
import com.hcl.entity.StatusMessage;
import com.hcl.entity.Transaction_Audit;
import com.hcl.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class CustomControllerTest {
    @InjectMocks
    CustomerController customerController;
    @Mock
    CustomerService customerService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp()
    {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Mockito.when(customerService.CreateAccount(1,"Pavani","SA","INR")).thenReturn(ReasonCodes.APPROVED);
        MockHttpServletResponse response = mockMvc.perform(post("/api/createAccount?customerId=1&customerName=Pavani&accountType=SA&currency=INR")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
//        System.out.println(re);
        assertEquals(ReasonCodes.APPROVED.toString().length()+2, response.getContentAsString().length());
    }

    @Test
    public void testaddBalance() throws Exception {
        Mockito.when(customerService.addBalnce(5,1,764542673)).thenReturn(ReasonCodes.APPROVED);
        MockHttpServletResponse response = mockMvc.perform(put("/api/addBalance?balance=5&customerId=1&accountNumber=764542673")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
//        System.out.println(re);
        assertEquals(ReasonCodes.APPROVED.toString().length()+2, response.getContentAsString().length());
    }

    @Test
    public void testGetBalance() throws Exception {
//        Mockito.when(customerService.getBalance(1, LocalDateTime.now(), "/core/customer?customerId=")).thenReturn(new CustomerDto());
        MockHttpServletResponse response = mockMvc.perform(get("/api/core/customer?customerId=1")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
//        System.out.println(response.);
//        assertEquals(null,response.getContentAsString());
    }

    @Test
    public void testGetBalanceException() throws Exception {
//        Mockito.when(customerService.getBalance(1, LocalDateTime.now(), "/core/customer?customerId=")).thenReturn(new CustomerDto());
        MockHttpServletResponse response = mockMvc.perform(get("/api/core/customer?customeId=1")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
//        assertEquals(null,response.getContentAsString());
    }

    @Test
    public void testGetBalanceException2() throws Exception {
//        Mockito.when(customerService.getBalance(1, LocalDateTime.now(), "/core/customer?customerId=")).thenReturn(new CustomerDto());
        MockHttpServletResponse response = mockMvc.perform(get("/api/core/customer?customerId=hjjdb")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
//        assertEquals(null,response.getContentAsString());
    }

    @Test
    public void testGetAccountInfo() throws Exception {
//        Mockito.when(customerService.getBalance(1, LocalDateTime.now(), "/core/customer?customerId=")).thenReturn(new CustomerDto());
        MockHttpServletResponse response = mockMvc.perform(get("/api/core/customer/53")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
//        assertEquals(null,response.getContentAsString());
    }

    @Test
    public void testGetAccountInfoException() throws Exception {
//        Mockito.when(customerService.getBalance(1, LocalDateTime.now(), "/core/customer?customerId=")).thenReturn(new CustomerDto());
        MockHttpServletResponse response = mockMvc.perform(get("/api/core/customer")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
//        assertEquals(null,response.getContentAsString());
    }

    @Test
    public void testGetTransactionAuditList() throws Exception {
        Transaction_Audit transactionAudit = Transaction_Audit.builder().id("hkgfshkg").iMessage("APPROVED").messgInTime(LocalDateTime.now()).messOutTime(LocalDateTime.now())
                .reasonCode(ReasonCodes.APPROVED).status(StatusMessage.SUCCESS).build();
        List<Transaction_Audit> transactionAuditList = new ArrayList<>();
        transactionAuditList.add(transactionAudit);
        Mockito.when(customerService.getTransactionAudit()).thenReturn(transactionAuditList);
        MockHttpServletResponse response = mockMvc.perform(get("/api/getTransactionAudit")).andReturn().getResponse();
        assertEquals(response.getStatus(), HttpStatus.OK.value());
        System.out.println(response.getContentAsString());
    }
}
