package com.skrrt.sktas.dto;

import java.math.BigDecimal;

public record BudgetSummaryResponse(
    BigDecimal totalAllocatedBudget,
    BigDecimal totalExpenses,
    BigDecimal remainingBudget,
    BigDecimal utilizationPercentage
) {
}
