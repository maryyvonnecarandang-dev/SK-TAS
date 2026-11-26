package com.skrrt.sktas.repository;

import com.skrrt.sktas.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByProject_ProjectId(Long projectId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
           "WHERE e.project.projectId = :projectId")
    BigDecimal getTotalExpensesByProjectId(@Param("projectId") Long projectId);
}
