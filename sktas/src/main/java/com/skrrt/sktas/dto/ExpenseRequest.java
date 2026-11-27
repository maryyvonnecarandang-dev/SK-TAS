package com.skrrt.sktas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
    Long projectId,
    String expenseName,
    String description,
    BigDecimal amount,
    LocalDate expenseDate
) {
}
