package com.thallesgarbelotti.task.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.thallesgarbelotti.task.dto.CreateTaskDTO;
import com.thallesgarbelotti.task.dto.UpdateTaskDTO;
import com.thallesgarbelotti.task.entity.Task;
import com.thallesgarbelotti.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.thallesgarbelotti.task.exception.GlobalExceptionHandler;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    private JsonMapper objectMapper;

    @Mock
    private TaskService service;

    @InjectMocks
    private TaskController controller;

    @BeforeEach
    void setUp() {
        this.objectMapper = JsonMapper.builder().findAndAddModules().build();
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    private Task taskWithId(String description, boolean finished, long id) {
        Task task = new Task(description);
        if (finished) task.markAsFinished();
        ReflectionTestUtils.setField(task, "id", id);
        return task;
    }

    @Test
    @DisplayName("POST /tasks retorna 201 e body correto quando input é válido")
    void createTaskShouldReturnCreated() throws Exception {
        CreateTaskDTO dto = new CreateTaskDTO("Nova tarefa");
        Task saved = taskWithId("Nova tarefa", false, 1L);
        when(service.create(any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tasks/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Nova tarefa"))
                .andExpect(jsonPath("$.finished").value(false));

        verify(service).create(any(Task.class));
    }

    @Test
    @DisplayName("POST /tasks com descrição inválida retorna 400")
    void createTaskWithInvalidBodyShouldReturnBadRequest() throws Exception {
        String payload = """
            {
              "description": ""
            }
            """;

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());

        verify(service, never()).create(any());
    }

    @Test
    @DisplayName("GET /tasks/{id} retorna 200 quando task existe")
    void listByIdShouldReturnTaskWhenExists() throws Exception {
        Task task = taskWithId("Minha task", false, 1L);
        when(service.listById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Minha task"))
                .andExpect(jsonPath("$.finished").value(false));
    }

    @Test
    @DisplayName("GET /tasks/{id} retorna 404 quando task não existe")
    void listByIdShouldReturnNotFoundWhenMissing() throws Exception {
        when(service.listById(1L)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /tasks retorna lista de tasks")
    void listShouldReturnTasks() throws Exception {
        Task first = taskWithId("A", false, 1L);
        Task second = taskWithId("B", true, 2L);
        when(service.list()).thenReturn(List.of(first, second));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].description").value("A"))
                .andExpect(jsonPath("$[1].description").value("B"));
    }

    @Test
    @DisplayName("PATCH /tasks/{id} retorna 200 quando atualização é bem sucedida")
    void updateShouldReturnOk() throws Exception {
        UpdateTaskDTO dto = new UpdateTaskDTO("Atualizada", true);
        Task updated = taskWithId("Atualizada", true, 1L);
        when(service.update(eq(1L), any(UpdateTaskDTO.class))).thenReturn(updated);

        mockMvc.perform(patch("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Atualizada"))
                .andExpect(jsonPath("$.finished").value(true));
    }

    @Test
    @DisplayName("PATCH /tasks/{id} retorna 404 quando task não existe")
    void updateShouldReturnNotFoundWhenMissing() throws Exception {
        UpdateTaskDTO dto = new UpdateTaskDTO("Atualizada", false);
        when(service.update(eq(1L), any(UpdateTaskDTO.class))).thenThrow(new NoSuchElementException());

        mockMvc.perform(patch("/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /tasks/{id} retorna 204 quando task existe")
    void deleteShouldReturnNoContent() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    @DisplayName("DELETE /tasks/{id} retorna 404 quando task não existe")
    void deleteShouldReturnNotFound() throws Exception {
        doThrow(new NoSuchElementException()).when(service).delete(1L);

        mockMvc.perform(delete("/tasks/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}
