package com.thallesgarbelotti.todo_list.controller;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.service.TaskService;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void create(@RequestBody @Valid Task newTask) {
        this.service.create(newTask);
    }

    @GetMapping("{id}")
    @ResponseStatus
    ResponseEntity<Task> listById(@PathVariable Long id) {
        try {
            var selectedTask = this.service.listById(id);
            return ResponseEntity.ok(selectedTask);
        } catch(NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    List<Task> list() {
        return this.service.list();
    }

    @PatchMapping("{id}")
    Task update(@PathVariable Long id, @RequestBody Task updatedTask) {
        return this.service.update(id, updatedTask);
    }

    @DeleteMapping("{id}")
    List<Task> delete(@PathVariable Long id) {
        return this.service.delete(id);
    }
}
