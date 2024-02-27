package com.hcl.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long accNum;
    private String actccy;
    private AccountType accType;
    private double balance;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cust_Id", referencedColumnName = "cust_Id")
    private Customer customer;
}
