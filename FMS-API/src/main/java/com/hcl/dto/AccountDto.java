package com.hcl.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcl.entity.AccountType;
import com.hcl.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonFilter("AccountFilter")
public class AccountDto {
    private long id;
//    @JsonIgnore
    private long accNum;
    private String actccy;
//    @JsonIgnore
    private AccountType accType;
    private double balance;
    @JsonIgnore
    private Customer customer;

//    public AccountDto()
//    {
//
//    }
}
