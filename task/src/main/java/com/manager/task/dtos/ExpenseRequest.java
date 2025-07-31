package com.manager.task.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExpenseRequest {
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private Long categoryId;
}