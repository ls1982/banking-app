package com.revolut.task.web.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OneAccountOperationRequestDto {
    @NotNull(message = "Account number can't be empty")
    private Long accountNumber;
    @NotNull(message = "Amount can't be empty")
    private BigDecimal amount;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
