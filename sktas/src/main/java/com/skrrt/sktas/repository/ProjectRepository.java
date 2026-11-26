package com.skrrt.sktas.repository;

import com.skrrt.sktas.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT COALESCE(SUM(p.allocatedBudget), 0) FROM Project p")
    BigDecimal getTotalAllocatedBudget();

}
