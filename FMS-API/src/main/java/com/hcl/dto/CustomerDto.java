package com.hcl.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonFilter("AccountFilter")
public class CustomerDto {
    private long custId;
//    @JsonIgnore
    private String cust_Name;
//    private List<AccountDto> accounts;
    private List<Object> accounts;

    public CustomerDto(long custId, String cust_Name)
    {
        this.custId  = custId;
        this.cust_Name = cust_Name;
    }
//    public CustomerDto(long custId, String cust_Name, List<Object> accountDtoList)
//    {
//        this.custId = custId;
//        this.cust_Name = cust_Name;
//        this.accounts = accountDtoList;
//    }

}
