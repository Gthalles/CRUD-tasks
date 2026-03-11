package com.thallesgarbelotti.task.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskDTO(
    @NotBlank String description
) {};
