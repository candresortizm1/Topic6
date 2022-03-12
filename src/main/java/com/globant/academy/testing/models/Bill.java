package com.globant.academy.testing.models;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Bill {
    String id;
    int companyId;
    LocalDate dueDate;
    BigDecimal amount;

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", companyId=" + companyId +
                ", dueDate=" + dueDate +
                ", amount=" + amount +
                '}';
    }
}
