package com.thallesgarbelotti.task.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class TaskEntityTest {
    @Test
    @DisplayName("Cria Task com descrição válida e finished padrão false")
    void createTaskWhenDescriptionIsValidShouldCreateEntity() {
        var description = "Create entity tests";
        Task task = new Task(description);

        assertNotNull(task);
        assertFalse(task.isFinished());
        assertEquals(description, task.getDescription());
    }

    @Test
    @DisplayName("Lança exceção ao criar Task com descrição em branco")
    void createTaskWhenDescriptionIsBlankShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Task("   "));
    }

    @Test
    @DisplayName("Lança exceção ao criar Task com descrição nula")
    void createTaskWhenDescriptionIsNullShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Task(null));
    }

    @Test
    @DisplayName("Atualiza descrição quando valor é válido")
    void setDescriptionWhenValidShouldUpdateEntity() {
        Task task = new Task("New task");
        var newDescription = "Create entity tests";
        task.setDescription(newDescription);

        assertEquals(newDescription, task.getDescription());
    }

    @Test
    @DisplayName("Lança exceção ao atualizar descrição com valor inválido")
    void setDescriptionWhenBlankShouldThrowException() {
        Task task = new Task("Initial description");

        assertThrows(IllegalArgumentException.class, () -> task.setDescription(""));
    }

    @Test
    @DisplayName("Marca task como finalizada")
    void markAsFinishedShouldSetFinishedTrue() {
        Task task = new Task("Task");
        task.markAsFinished();

        assertTrue(task.isFinished());
    }

    @Test
    @DisplayName("Marca task como pendente")
    void markAsPendingShouldSetFinishedFalse() {
        Task task = new Task("Task");
        task.markAsFinished();
        task.markAsPending();

        assertFalse(task.isFinished());
    }
}
