package com.revolut.task.web.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferRequestDto {
    @NotNull(message = "Destination account not specified")
    private Long accountTo;
    @NotNull(message = "amount can't be empty")
    private BigDecimal amount;

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
