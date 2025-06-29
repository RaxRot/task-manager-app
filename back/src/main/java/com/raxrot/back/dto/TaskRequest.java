package com.raxrot.back.dto;

import com.raxrot.back.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {
    private Long id;
    @NotBlank(message = "title cannot be empty")
    @Size(max = 50,message = "title must be less then 50 characters")
    private String title;

    @Size(max = 500,message = "title must be less then 500 characters")
    private String description;

    @NotNull(message = "competed status is required")
    private Boolean completed;

    @NotNull(message = "priority is required")
    private Priority priority;

    @FutureOrPresent(message = "due date must be today or in future")
    private LocalDate dueDate;
}
