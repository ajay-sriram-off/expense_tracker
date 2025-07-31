package com.manager.task.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @NotBlank(message = "Description cannot be empty")
    private String name;

}
