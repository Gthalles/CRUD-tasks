package com.thallesgarbelotti.task.dto;

public record ResponseTaskDTO(
        Long id,
        String description,
        boolean finished
) {};
