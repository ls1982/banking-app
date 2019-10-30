package com.revolut.task.web.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AmountRequestDto {
    @NotNull(message = "Amount can't be empty")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
