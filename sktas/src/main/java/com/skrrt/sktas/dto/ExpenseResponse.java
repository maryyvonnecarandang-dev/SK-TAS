package com.skrrt.sktas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseResponse(
    Long expenseId,
    Long projectId,
    String expenseName,
    String description,
    BigDecimal amount,
    LocalDate expenseDate,
    LocalDateTime createdAt
) {
}
