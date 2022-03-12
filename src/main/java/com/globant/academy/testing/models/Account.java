package com.globant.academy.testing.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    int id;
    String type;
    String firstName;
    String lastName;
    String documentNumber;
    BigDecimal balance;
    int bankId;
    String bankName;

    @Override
    public String toString() {
        return "Account{" +
                "type='" + type + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", documentNumber='" + documentNumber + '\'' +
                ", balance=" + balance +
                ", bankId=" + bankId +
                ", bankName='" + bankName + '\'' +
                '}';
    }
}
