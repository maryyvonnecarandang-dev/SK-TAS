
package com.skrrt.sktas.controller;

import com.skrrt.sktas.model.Expense;
import com.skrrt.sktas.model.Project;
import com.skrrt.sktas.repository.ExpenseRepository;
import com.skrrt.sktas.repository.ProjectRepository;
import com.skrrt.sktas.dto.ExpenseRequest;
import com.skrrt.sktas.dto.ExpenseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final ProjectRepository projectRepository;

    public ExpenseController(ExpenseRepository expenseRepository,
                             ProjectRepository projectRepository) {
        this.expenseRepository = expenseRepository;
        this.projectRepository = projectRepository;
    }

    // READ - Retrieve all expenses
    @GetMapping
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // READ - Retrieve an individual expense
    @GetMapping("/{id}")
    public ExpenseResponse getExpenseById(@PathVariable Long id) {
        return toResponse(findExpense(id));
    }

    // READ - Retrieve expenses by project
    @GetMapping("/project/{projectId}")
    public List<ExpenseResponse> getExpensesByProject(
            @PathVariable Long projectId) {

        findProject(projectId);

        return expenseRepository.findByProject_ProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // CREATE - Add an expense
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse createExpense(
            @RequestBody ExpenseRequest request) {

        validateExpense(request);

        Project project = findProject(request.projectId());

        Expense expense = new Expense(
                project,
                request.expenseName(),
                request.description(),
                request.amount(),
                request.expenseDate()
        );

        return toResponse(expenseRepository.save(expense));
    }

    // UPDATE - Modify an expense
    @PutMapping("/{id}")
    public ExpenseResponse updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request) {

        Expense expense = findExpense(id);

        validateExpense(request);

        Project project = findProject(request.projectId());

        expense.setProject(project);
        expense.setExpenseName(request.expenseName());
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setExpenseDate(request.expenseDate());

        return toResponse(expenseRepository.save(expense));
    }

    // DELETE - Remove an expense
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable Long id) {
        expenseRepository.delete(findExpense(id));
    }

    // Helper method - Find an expense
    private Expense findExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Expense not found"));
    }

    // Helper method - Find a project
    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));
    }

    // Validate expense information
    private void validateExpense(ExpenseRequest request) {

        if (request.projectId() == null
                || request.projectId() <= 0
                || request.expenseName() == null
                || request.expenseName().isBlank()
                || request.expenseName().length() > 150
                || request.amount() == null
                || request.amount().signum() <= 0
                || request.amount().stripTrailingZeros().scale() > 2
                || request.amount().compareTo(
                    new BigDecimal("9999999999.99")) > 0
                || request.expenseDate() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid expense information");
        }
    }

    // Convert entity into response DTO
    private ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getExpenseId(),
                expense.getProject().getProjectId(),
                expense.getExpenseName(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getExpenseDate(),
                expense.getCreatedAt()
        );
    }
}
