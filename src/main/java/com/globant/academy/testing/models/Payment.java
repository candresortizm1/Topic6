package com.globant.academy.testing.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Payment extends Transaction{

    int idCompany;
    String invoiceId;
    Date dueDate;

    @Override
    public void setTransactionType(String transactionType) {
        super.setTransactionType("payment");
    }

}
