package com.globant.academy.testing.generators;

import com.globant.academy.testing.models.Bill;

public interface BillGenerator {
    Bill findInvoiceByCompanyAndId(String id, int companyId);
}
