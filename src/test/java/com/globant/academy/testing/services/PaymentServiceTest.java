package com.globant.academy.testing.services;

import com.globant.academy.testing.exception.InsufficientFundsException;
import com.globant.academy.testing.generators.AccountGenerator;
import com.globant.academy.testing.generators.BillGenerator;
import com.globant.academy.testing.models.Account;
import com.globant.academy.testing.models.Bill;
import com.globant.academy.testing.models.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentServiceTest {
    @Mock
    private AccountGenerator acGenerator;

    @Mock
    private BillGenerator invoiceGenerator;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAccountWithoutBalance() {
        Account account = Account.builder().id(1).balance(new BigDecimal(100_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("101331659").build();
         Bill bill = Bill.builder().id("1").companyId(1)
                .dueDate(LocalDate.of(2014, 2, 14))
                .amount(new BigDecimal(100_001)).build();
        when(acGenerator.findAccountByBankTypeAndId(
                account.getId(),account.getBankId(),account.getType())).thenReturn(account);
        when(invoiceGenerator.findInvoiceByCompanyAndId(bill.getId(),bill.getCompanyId())).thenReturn(bill);

        InsufficientFundsException ex =
                Assertions.assertThrows(InsufficientFundsException.class,
                        () -> paymentService.pay(account.getId(),account.getBankId(),account.getType(),
                                bill.getCompanyId(),bill.getId()));

        Assertions.assertEquals(PaymentService.INSUFICIENT_FUNDS_MESSAGE,
                ex.getMessage());
    }

    @Test
    void testBillWithDiscount() {
        Account account = Account.builder().id(1).balance(new BigDecimal(100_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("CORRIENTE").documentNumber("101331659").build();
        Bill bill = Bill.builder().id("1").companyId(1)
                .dueDate(LocalDate.of(2014, 2, 14))
                .amount(new BigDecimal(100_001)).build();
        when(acGenerator.findAccountByBankTypeAndId(
                account.getId(),account.getBankId(),account.getType())).thenReturn(account);
        when(invoiceGenerator.findInvoiceByCompanyAndId(bill.getId(),bill.getCompanyId())).thenReturn(bill);

        BigDecimal billWithDiscount = bill.getAmount().multiply(new BigDecimal(0.9));
        Payment  payment = paymentService.pay(account.getId(),account.getBankId(),account.getType(),
                bill.getCompanyId(),bill.getId());
        Assertions.assertEquals(payment.getAmount(),billWithDiscount);
    }
}
