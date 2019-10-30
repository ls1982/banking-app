package com.revolut.task.model;

import java.math.BigDecimal;

/**
 * Bank transaction simplest representation
 *
 * @author Alexey Smirnov
 * */
public final class Operation {
    private final Long id;
    /** Source account number */
    private final Long accountFrom;
    /** Destination account number */
    private final Long accountTo;
    /** Operation type */
    private final Type type;
    /** Amount of funds */
    private final BigDecimal amount;

    /** Operation type representation */
    public enum Type {
        DEPOSIT, // Putting money
        WITHDRAW, // Getting money from account
        TRANSFER // Transfer money between two accounts
    }

    public Operation(Long id, Long accountFrom, Long accountTo, Type type, BigDecimal amount) {
        this.id = id;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.type = type;
        this.amount = amount;
    }

    public static Operation deposit(Long accountNumber, BigDecimal amount) {
        return new Operation(null, null, accountNumber, Type.DEPOSIT, amount);
    }

    public static Operation withdrawal(Long accountNumber, BigDecimal amount) {
        return new Operation(null, accountNumber, null, Type.WITHDRAW, amount);
    }

    public static Operation transfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        return new Operation(null, accountFrom, accountTo, Type.TRANSFER, amount);
    }

    public Long getId() {
        return id;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
