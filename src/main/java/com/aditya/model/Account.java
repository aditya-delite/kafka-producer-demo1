package com.aditya.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {
    private int accountId;
    private String type;
    private double balance;
    private String currency;
}
