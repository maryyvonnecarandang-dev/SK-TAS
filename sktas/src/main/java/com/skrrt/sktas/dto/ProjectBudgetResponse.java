package com.skrrt.sktas.dto;

import java.math.BigDecimal;

public record ProjectBudgetResponse(
    Long projectId,
    String projectName,
    BigDecimal allocatedBudget,
    BigDecimal totalExpenses,
    BigDecimal remainingBudget,
    BigDecimal utilizationPercentage
) {
}
