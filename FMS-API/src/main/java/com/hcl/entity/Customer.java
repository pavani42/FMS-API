package com.hcl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long cust_Id;
    private String cust_Name;
    @OneToMany(mappedBy = "customer")
    private List<Account> accountList;

    public Customer(long cust_Id, String cust_Name)
    {
        this.cust_Id = cust_Id;
        this.cust_Name = cust_Name;
    }
}
