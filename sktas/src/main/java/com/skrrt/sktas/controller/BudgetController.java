
package com.skrrt.sktas.controller;

import com.skrrt.sktas.model.Project;
import com.skrrt.sktas.repository.ProjectRepository;
import com.skrrt.sktas.repository.ExpenseRepository;
import com.skrrt.sktas.dto.BudgetSummaryResponse;
import com.skrrt.sktas.dto.ProjectBudgetResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/budgets")
@Transactional(readOnly = true)
public class BudgetController {

    private final ProjectRepository projectRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetController(
            ProjectRepository projectRepository,
            ExpenseRepository expenseRepository) {

        this.projectRepository = projectRepository;
        this.expenseRepository = expenseRepository;
    }

    // Overall budget summary
    @GetMapping("/summary")
    public BudgetSummaryResponse getBudgetSummary() {

        BigDecimal allocated =
                projectRepository.getTotalAllocatedBudget();

        BigDecimal expenses =
                expenseRepository.getTotalExpenses();

        BigDecimal remaining = allocated.subtract(expenses);

        BigDecimal utilization =
                calculateUtilization(allocated, expenses);

        return new BudgetSummaryResponse(
                allocated,
                expenses,
                remaining,
                utilization
        );
    }

    // Individual project budget summary
    @GetMapping("/project/{id}")
    public ProjectBudgetResponse getProjectBudget(
            @PathVariable Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found"));

        BigDecimal allocated = project.getAllocatedBudget();

        BigDecimal expenses =
                expenseRepository.getTotalExpensesByProjectId(id);

        BigDecimal remaining = allocated.subtract(expenses);

        BigDecimal utilization =
                calculateUtilization(allocated, expenses);

        return new ProjectBudgetResponse(
                project.getProjectId(),
                project.getProjectName(),
                allocated,
                expenses,
                remaining,
                utilization
        );
    }

    // Calculate budget utilization
    private BigDecimal calculateUtilization(
            BigDecimal allocated, BigDecimal expenses) {

        if (allocated.signum() <= 0) {
            return null;
        }

        return expenses
                .multiply(new BigDecimal("100"))
                .divide(allocated, 2, RoundingMode.HALF_UP);
    }
}
