package com.thallesgarbelotti.task.repository;

import com.thallesgarbelotti.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
