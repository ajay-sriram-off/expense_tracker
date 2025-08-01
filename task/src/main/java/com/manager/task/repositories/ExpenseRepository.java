package com.manager.task.repositories;

import com.manager.task.entities.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    Page<Expense> findByCategoryId(Long categoryId, Pageable pageable);
}
