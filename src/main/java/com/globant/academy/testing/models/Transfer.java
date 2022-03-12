package com.globant.academy.testing.models;

import lombok.AccessLevel;
import lombok.Setter;

public class Transfer extends Transaction{

    int accountReceiptId;
    int bankId;

    @Override
    public void setTransactionType(String transactionType) {
        super.setTransactionType("transference");
    }
}
