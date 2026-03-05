package com.thallesgarbelotti.todo_list;

import com.thallesgarbelotti.todo_list.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TodoListApplicationTests {
	private final WebApplicationContext context;
	private WebTestClient webTestClient;

	TodoListApplicationTests(WebApplicationContext context) {
		this.context = context;
	}

	@BeforeEach
	void setUp() {
		this.webTestClient = MockMvcWebTestClient.bindToApplicationContext(this.context).build();
	}

	@Test
	void createTaskWithSuccess() {
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
	void createTaskWithFailure() {
		var task = new Task();

		webTestClient.post().uri("/tasks")
				.bodyValue(task)
				.exchange()
				.expectStatus()
				.isBadRequest();
	}
}
