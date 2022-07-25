package com.yandexpraktikum.tasktracker.test;

import com.google.gson.Gson;
import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.HttpTaskServer;
import com.yandexpraktikum.tasktracker.service.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskServerTest {

    private HttpTaskServer server;
    private KVServer kvServer;
    private static final String ADDRESS = "http://localhost:8080/";

    @BeforeEach
    private void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            server = new HttpTaskServer();
            server.start();
        } catch (IOException exception) {
            System.out.println("Ошибка создания сервера.");
        }
    }

    @AfterEach
    private void afterEach() {
        server.stop();
        kvServer.stop();
    }

    @Test
    public void shouldCreateTask() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldCreateEpic() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldCreateSubTask() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW", 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetTasks() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting tasks
            URI url1 = URI.create(ADDRESS + "tasks/task/");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetEpics() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting epics
            URI url1 = URI.create(ADDRESS + "tasks/epic/");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetSubTasks() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW", 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //getting subtasks
            URI url2 = URI.create(ADDRESS + "tasks/subtask/");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetTaskByID() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting task by id
            URI url1 = URI.create(ADDRESS + "tasks/task/?id=1");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetEpicByID() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting epic by id
            URI url1 = URI.create(ADDRESS + "tasks/epic/?id=1");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetSubTaskByID() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW", 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //getting subtask by id
            URI url2 = URI.create(ADDRESS + "tasks/subtask/?id=1");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteAllTasks() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //deleting tasks
            URI url1 = URI.create(ADDRESS + "tasks/task/");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteAllEpics() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //deleting epics
            URI url1 = URI.create(ADDRESS + "tasks/epic/");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteAllSubTasks() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW",
                    2, LocalDateTime.now().plusMinutes(2), 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //deleting subtasks
            URI url2 = URI.create(ADDRESS + "tasks/subtask/");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteTaskByID() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //deleting task by id
            URI url1 = URI.create(ADDRESS + "tasks/task/?id=2");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteEpicByID() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //deleting epic by id
            URI url1 = URI.create(ADDRESS + "tasks/epic/?id=1");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).DELETE().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldDeleteSubTaskByID() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW", 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //deleting subtask by id
            URI url2 = URI.create(ADDRESS + "tasks/subtask/?id=2");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).DELETE().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetEpicSubTasks() {
        try {
            //adding epic
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/epic/");
            Epic newEpic = new Epic("name", "description");
            Gson gson = new Gson();
            String json = gson.toJson(newEpic);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //adding subtask
            URI url1 = URI.create(ADDRESS + "tasks/subtask/");
            SubTask newSubTask = new SubTask("name1", "description1", "NEW", 1);
            String json1 = gson.toJson(newSubTask);
            final HttpRequest.BodyPublisher body1 = HttpRequest.BodyPublishers.ofString(json1);
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).POST(body1).build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //getting subtasks by epic id
            URI url2 = URI.create(ADDRESS + "tasks/subtask/epic/?id=1");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetHistory() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting task by id
            URI url1 = URI.create(ADDRESS + "tasks/task/?id=1");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            //getting history
            URI url2 = URI.create(ADDRESS + "tasks/history");
            HttpRequest request2 = HttpRequest.newBuilder().uri(url2).GET().build();
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response2.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }

    @Test
    public void shouldGetPrioritizedTasks() {
        try {
            //adding task
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(ADDRESS + "tasks/task/");
            Gson gson = new Gson();
            Task newTask = new Task("name", "description", "NEW");
            String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //getting sortedset
            URI url1 = URI.create(ADDRESS + "tasks/");
            HttpRequest request1 = HttpRequest.newBuilder().uri(url1).GET().build();
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response1.statusCode());
        } catch (IOException|InterruptedException exception) {
            System.out.println("Ошибка.");
        }
    }
}
