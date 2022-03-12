package com.globant.academy.testing.services;

import com.globant.academy.testing.exception.InsufficientFundsException;
import com.globant.academy.testing.exception.InvalidTargetFundException;
import com.globant.academy.testing.generators.AccountGenerator;
import com.globant.academy.testing.models.Account;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class TransferService {
    public static String INSUFICIENT_FUNDS_MESSAGE = "FONDOS INSUFICIENTES";
    public static String INVALID_TARGET_MESSAGE = "CUENTA CORRIENTE DESTINO CON MUCHO SALDO";
    private static final BigDecimal INTERBANK_COST = new BigDecimal(3500);
    private static final BigDecimal MULT_MAX_BALANCE_RECEIPT = new BigDecimal(3);
    private static final BigDecimal MAX_VALUE_WITHOUT_TAX = new BigDecimal(1_500_000);
    private static final BigDecimal AMOUNT_AFTER_TAX = new BigDecimal(0.97);

    @Autowired
    private AccountGenerator accountGenerator;

    public boolean transfer(int idOriginAccount,int bankIdOriginAccount,String typeOriginAccount,
                         int idReceiptAccount,int bankIdReceiptAccount,String typeReceiptAccount, BigDecimal amount){
        Account accountOrigin = accountGenerator.findAccountByBankTypeAndId(idOriginAccount,bankIdOriginAccount,typeOriginAccount);
        Account accountReceipt = accountGenerator.findAccountByBankTypeAndId(idReceiptAccount,bankIdReceiptAccount,typeReceiptAccount);
        originAccountHasFunds(accountOrigin,accountReceipt,amount);
        if(accountReceipt.getType().equals("CORRIENTE")){
            receptAccountValidation(accountReceipt,amount);
        }
        if(amount.compareTo(MAX_VALUE_WITHOUT_TAX)==1){
            amount = amount.multiply(AMOUNT_AFTER_TAX);
        }

        return true;
    }

    private void receptAccountValidation(Account accountReceipt, BigDecimal amount) throws InvalidTargetFundException {
        BigDecimal maxValidBalance = amount.multiply(MULT_MAX_BALANCE_RECEIPT);
        if(accountReceipt.getBalance().compareTo(maxValidBalance)==1){
            throw new InvalidTargetFundException(INVALID_TARGET_MESSAGE);
        }
    }

    private void originAccountHasFunds(Account originAccount, Account receiptAccount,
                                       BigDecimal amount) throws InsufficientFundsException{
        if(originAccount.getBankId()!=receiptAccount.getBankId()){
            amount = amount.add(INTERBANK_COST);
        }
        BigDecimal min = originAccount.getBalance();
        //Compare the balance int origin account with the amount to transfer
        if(min.compareTo(amount)==-1){
            throw new InsufficientFundsException(INSUFICIENT_FUNDS_MESSAGE);
        }
    }

}
