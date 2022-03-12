package com.globant.academy.testing.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public abstract class Transaction {
    int id;
    @Setter(AccessLevel.NONE)
    String dateTime;
    int accountOrigin;
    BigDecimal amount;
    String transactionType;

    private void setDateTime(DateTimeFormatter dateTime) {
        this.dateTime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
    }

}
