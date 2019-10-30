package com.revolut.task.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

/**
 * Bank account simplest representation
 *
 * @author Alexey Smirnov
 * */
public class Account {
    /** Account number. Generated automatically when account is created */
    private final Long accountNumber;
    /** Amount */
    private BigDecimal balance;

    @JsonCreator
    public Account(@JsonProperty("accountNumber") Long accountNumber, @JsonProperty("balance") BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
