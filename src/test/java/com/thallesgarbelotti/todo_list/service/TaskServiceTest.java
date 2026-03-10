package com.thallesgarbelotti.todo_list.service;

import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    void shouldCreateTask() {}

    @Test
    void shouldListTasksOrderedByDescription() {}

    @Test
    void shouldReturnTaskByIdWhenTaskExists() {}

    @Test
    void shouldThrowNoSuchElementExceptionWhenTaskDoesNotExist() {}

    @Test
    void shouldUpdateTaskWhenTaskExists() {}

    @Test
    void shouldThrowNoSuchElementExceptionWhenUpdatingTaskThatDoesNotExist() {}

    @Test
    void shouldDeleteTaskById() {}
}