package com.thallesgarbelotti.todo_list;

import com.thallesgarbelotti.todo_list.repository.TaskRepository;
import com.thallesgarbelotti.todo_list.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.thallesgarbelotti.todo_list.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoListApplicationTests {
	private WebTestClient webTestClient;
	private final WebApplicationContext context;

	@Autowired
	private TaskRepository repository;

	TodoListApplicationTests(WebApplicationContext context) {
        this.context = context;
	}

	@BeforeEach
	void setUpWebTestClient() {
		this.webTestClient = MockMvcWebTestClient.bindToApplicationContext(this.context).build();
	}

	@Test
	void createTaskWhenInputIsValidShouldReturnCreated() {
		var task = new Task();
		task.setDescription("test");
		task.setFinished(false);

			webTestClient.post()
					.uri("/tasks")
					.bodyValue(task)
					.exchange()
					.expectStatus()
					.isCreated();
	}

	@Test
	void createTaskWhenInputIsInvalidShouldReturnBadRequest() {
		var task = new Task();

		webTestClient.post().uri("/tasks")
				.bodyValue(task)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}

	@Test
	void listTaskByIdWhenTaskExistShouldReturnTaskAndOk() {
		var task = new Task();
		task.setDescription("New task");
		Task savedTask = repository.save(task);

		webTestClient
				.get()
				.uri("/tasks/{id}", savedTask.getId())
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.id").isEqualTo(savedTask.getId())
				.jsonPath("$.description").isEqualTo(savedTask.getDescription())
				.jsonPath("$.finished").isEqualTo(savedTask.isFinished());
	}

	@Test
	void listTaskByIdWhenTaskNotExistShouldReturnNotFound() {
		webTestClient
				.get()
				.uri("/tasks/{id}", 9999)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void listTasksWhenTasksExistsShouldReturnTasksAndOk() {
		var firstTask = new Task();
		firstTask.setDescription("first");
		var secondTask = new Task();
		secondTask.setDescription("second");

		repository.saveAllAndFlush(List.of(firstTask, secondTask));

		webTestClient
				.get()
				.uri("/tasks")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(2);
	}

	@Test
	void listTasksWhenThereAreNoTasksShouldReturnEmptyArrayAndOk() {
		webTestClient
				.get()
				.uri("/tasks")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(0);
	}
}
