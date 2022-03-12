package com.globant.academy.testing.services;

import com.globant.academy.testing.exception.InsufficientFundsException;
import com.globant.academy.testing.generators.AccountGenerator;
import com.globant.academy.testing.generators.BillGenerator;
import com.globant.academy.testing.models.Account;
import com.globant.academy.testing.models.Bill;
import com.globant.academy.testing.models.Payment;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PaymentService {
    public static String INSUFICIENT_FUNDS_MESSAGE = "FONDOS INSUFICIENTES";
    private static final BigDecimal AMOUNT_AFTER_DISCOUNT = new BigDecimal(0.9);
    private static final BigDecimal RATE_NEEDED_BALANCE_TO_PAY = new BigDecimal(1.2);

    @Autowired
    private AccountGenerator accountGenerator;

    @Autowired
    private BillGenerator billGenerator;

    public Payment pay(int accountId, int accountBankId, String typeAccount, int companyId, String invouceId){
        Account account = accountGenerator.findAccountByBankTypeAndId(accountId,accountBankId, typeAccount);
        Bill bill = billGenerator.findInvoiceByCompanyAndId(invouceId,companyId);
        BigDecimal amount = bill.getAmount();
        if(account.getType().equals("CORRIENTE")){
            amount = amount.multiply(AMOUNT_AFTER_DISCOUNT);
        }
        Payment payment = new Payment();
        payment.setAmount(amount);
        originAccountHasFunds(account,amount);

        return payment;

    }

    private void originAccountHasFunds(Account account, BigDecimal amount) throws InsufficientFundsException{
        BigDecimal balance = amount.multiply(RATE_NEEDED_BALANCE_TO_PAY);
        if(account.getBalance().compareTo(balance)==-1){
            throw new InsufficientFundsException(INSUFICIENT_FUNDS_MESSAGE);
        }
    }
}
