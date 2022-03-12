package com.globant.academy.testing.generators;

import com.globant.academy.testing.models.Account;

public interface AccountGenerator {
    Account findAccountByBankTypeAndId(int id,int bankId,String type);
}
