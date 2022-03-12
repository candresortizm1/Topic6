package com.globant.academy.testing.services;

import com.globant.academy.testing.exception.InsufficientFundsException;
import com.globant.academy.testing.exception.InvalidTargetFundException;
import com.globant.academy.testing.generators.AccountGenerator;
import com.globant.academy.testing.models.Account;
import com.globant.academy.testing.models.Transfer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TransferServiceTest {
    @Mock
    private AccountGenerator acGenerator;

    @InjectMocks
    private TransferService transference;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAccountWithoutFundsLocalBank() {
        Account account1 = Account.builder().id(1).balance(new BigDecimal(100_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("101331659").build();
        Account account2 = Account.builder().id(2).balance(new BigDecimal(300_000))
                .firstName("Andrés").lastName("Ortiz").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("1013616596").build();

        when(acGenerator.findAccountByBankTypeAndId(
                account2.getId(),account2.getBankId(),account2.getType())).thenReturn(account2);
        when(acGenerator.findAccountByBankTypeAndId(
                account1.getId(),account1.getBankId(),account1.getType())).thenReturn(account1);

        BigDecimal amount = new BigDecimal(101_000);

        InsufficientFundsException ex =
                Assertions.assertThrows(InsufficientFundsException.class,
                        () -> transference.transfer(account1.getId(),account1.getBankId(),account1.getType(),
                                account2.getId(),account2.getBankId(),account2.getType(), amount));

        Assertions.assertEquals(PaymentService.INSUFICIENT_FUNDS_MESSAGE,
                ex.getMessage());
    }

    @Test
    void testAccountWithoutFundsOtherBank() {
        Account account1 = Account.builder().id(1).balance(new BigDecimal(100_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("101331659").build();
        Account account2 = Account.builder().id(2).balance(new BigDecimal(300_000))
                .firstName("Andrés").lastName("Ortiz").bankName("Bancolombia")
                .bankId(1).type("AHORROS").documentNumber("1013616596").build();

        when(acGenerator.findAccountByBankTypeAndId(
                account2.getId(),account2.getBankId(),account2.getType())).thenReturn(account2);
        when(acGenerator.findAccountByBankTypeAndId(
                account1.getId(),account1.getBankId(),account1.getType())).thenReturn(account1);

        BigDecimal amount = new BigDecimal(99_000);

        InsufficientFundsException ex =
                Assertions.assertThrows(InsufficientFundsException.class,
                        () -> transference.transfer(account1.getId(),account1.getBankId(),account1.getType(),
                                account2.getId(),account2.getBankId(),account2.getType(), amount));

        Assertions.assertEquals(TransferService.INSUFICIENT_FUNDS_MESSAGE,
                ex.getMessage());
    }

    @Test
    void testInvalidTargetFunds() {
        Account account1 = Account.builder().id(1).balance(new BigDecimal(800_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("101331659").build();
        Account account2 = Account.builder().id(2).balance(new BigDecimal(300_000))
                .firstName("Andrés").lastName("Ortiz").bankName("Bancolombia")
                .bankId(1).type("CORRIENTE").documentNumber("1013616596").build();

        when(acGenerator.findAccountByBankTypeAndId(
                account2.getId(),account2.getBankId(),account2.getType())).thenReturn(account2);
        when(acGenerator.findAccountByBankTypeAndId(
                account1.getId(),account1.getBankId(),account1.getType())).thenReturn(account1);

        BigDecimal amount = new BigDecimal(60_000);

        InvalidTargetFundException ex =
                Assertions.assertThrows(InvalidTargetFundException.class,
                        () -> transference.transfer(account1.getId(),account1.getBankId(),account1.getType(),
                                account2.getId(),account2.getBankId(),account2.getType(), amount));

        Assertions.assertEquals(TransferService.INVALID_TARGET_MESSAGE,
                ex.getMessage());
    }

    @Test
    void testTaxDiscount() {
        Account account1 = Account.builder().id(1).balance(new BigDecimal(8_000_000))
                .firstName("Vivian").lastName("Bedoya").bankName("Globant")
                .bankId(5).type("AHORROS").documentNumber("101331659").build();
        Account account2 = Account.builder().id(2).balance(new BigDecimal(300_000))
                .firstName("Andrés").lastName("Ortiz").bankName("Bancolombia")
                .bankId(1).type("AHORROS").documentNumber("1013616596").build();

        when(acGenerator.findAccountByBankTypeAndId(
                account2.getId(),account2.getBankId(),account2.getType())).thenReturn(account2);
        when(acGenerator.findAccountByBankTypeAndId(
                account1.getId(),account1.getBankId(),account1.getType())).thenReturn(account1);

        BigDecimal amount = new BigDecimal(1_505_000);

        InvalidTargetFundException ex =
                Assertions.assertThrows(InvalidTargetFundException.class,
                        () -> transference.transfer(account1.getId(),account1.getBankId(),account1.getType(),
                                account2.getId(),account2.getBankId(),account2.getType(), amount));
    }
}
