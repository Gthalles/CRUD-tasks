package com.thallesgarbelotti.todo_list.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.thallesgarbelotti.todo_list.entity.Task;
import com.thallesgarbelotti.todo_list.service.TaskService;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService taskService) {
        this.service = taskService;
    }

    @PostMapping
    ResponseEntity<Task> create(@RequestBody @Valid Task newTask) {
            var savedTask = this.service.create(newTask);
            return ResponseEntity.created(URI.create("/tasks/" + savedTask.getId())).body(savedTask);
    }

    @GetMapping("/{id}")
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

    @PatchMapping("/{id}")
    ResponseEntity<Task> update(@PathVariable Long id, @RequestBody @Valid Task updatedTask) {
        try {
            var savedTask = this.service.update(id, updatedTask);
            return ResponseEntity.ok(savedTask);
        } catch (NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            this.service.listById(id);
            this.service.delete(id);
           return ResponseEntity.noContent().build();
        } catch (NoSuchElementException err) {
            return ResponseEntity.notFound().build();
        }
    }
}
