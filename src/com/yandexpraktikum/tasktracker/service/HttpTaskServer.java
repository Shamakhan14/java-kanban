package com.yandexpraktikum.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();
    private final HttpServer server;
    private static TaskManager fileBackedTasksManager;

    public HttpTaskServer() throws IOException {
        fileBackedTasksManager = new FileBackedTasksManager("save.txt");
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubTaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
        server.start();
    }

    static class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            if (path.equals("/tasks/")) {
                httpExchange.sendResponseHeaders( 200, 0);
                try (OutputStream outputStream = httpExchange.getResponseBody()) {
                    outputStream.write(gson.toJson(fileBackedTasksManager.getSortedSet()).getBytes());
                }
            }
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Task task = gson.fromJson(body, Task.class);
                    boolean isUpdated = false;
                    for (Task task1: fileBackedTasksManager.getTasks()) {
                        if (task1.getId() == task.getId()) {
                            fileBackedTasksManager.updateTask(task);
                            isUpdated = true;
                        }
                    }
                    if (!isUpdated) {
                        fileBackedTasksManager.addTask(task);
                    }
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "GET":
                    if (path.endsWith("/task/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getTasks()).getBytes());
                        }
                    }
                    if (path.contains("/?id=")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getTaskById(id)).getBytes());
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/task/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.clearAllTasks();
                    }
                    if (path.contains("/?id=")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.removeTaskById(id);
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
            }
            httpExchange.close();
        }
    }

    static class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    Epic epic = gson.fromJson(body, Epic.class);
                    boolean isUpdated = false;
                    for (Epic epic1: fileBackedTasksManager.getEpics()) {
                        if (epic1.getId() == epic.getId()) {
                            fileBackedTasksManager.updateEpic(epic);
                            isUpdated = true;
                        }
                    }
                    if (!isUpdated) {
                        fileBackedTasksManager.addEpic(epic);
                    }
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "GET":
                    if (path.endsWith("/epic/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getEpics()).getBytes());
                        }
                    }
                    if (path.contains("/?id=")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getEpicById(id)).getBytes());
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/epic/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.clearAllEpics();
                    }
                    if (path.contains("/?id=")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.removeEpicById(id);
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
            }
            httpExchange.close();
        }
    }

    static class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            switch (method) {
                case "POST":
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    boolean isUpdated = false;
                    for (SubTask subTask1: fileBackedTasksManager.getSubTasks()) {
                        if (subTask1.getId() == subTask.getId()) {
                            fileBackedTasksManager.updateSubTask(subTask);
                            isUpdated = true;
                        }
                    }
                    if (!isUpdated) {
                        fileBackedTasksManager.addSubTask(subTask);
                    }
                    httpExchange.sendResponseHeaders(201, 0);
                    break;
                case "GET":
                    if (path.contains("epic")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getSubtasksByEpicId(id)).getBytes());
                        }
                    }
                    if (path.endsWith("/subtask/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getSubTasks()).getBytes());
                        }
                    }
                    if (path.contains("/?id=") && !path.contains("epic")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        try (OutputStream outputStream = httpExchange.getResponseBody()) {
                            outputStream.write(gson.toJson(fileBackedTasksManager.getSubtaskById(id)).getBytes());
                        }
                    }
                    break;
                case "DELETE":
                    if (path.endsWith("/subtask/")) {
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.clearAllSubtasks();
                    }
                    if (path.contains("/?id=")) {
                        int id = Integer.parseInt(path.split("=")[1]);
                        httpExchange.sendResponseHeaders(200, 0);
                        fileBackedTasksManager.removeSubtaskById(id);
                    }
                    break;
                default:
                    httpExchange.sendResponseHeaders(404, 0);
            }
            httpExchange.close();
        }
    }

    static class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            httpExchange.sendResponseHeaders( 200, 0);
            try (OutputStream outputStream = httpExchange.getResponseBody()) {
                outputStream.write(gson.toJson(fileBackedTasksManager.getHistory()).getBytes());
            }
        }
    }

    public void stop() {
        server.stop(0);
    }

    public static void main(String[] args) {
        try {
            HttpTaskServer server1 = new HttpTaskServer();
            Gson gson1 = new Gson();
            Task task = new Task("N", "D", "NEW");
            System.out.println(gson1.toJson(task));
            Epic epic = new Epic("N2", "D2");
            System.out.println(gson1.toJson(epic));
            SubTask subTask = new SubTask("N3", "D3", "NEW", 2);
            System.out.println(gson1.toJson(subTask));
            SubTask subTask1 = new SubTask("N4", "D4", "NEW", 2);
            FileBackedTasksManager manager = new FileBackedTasksManager("save.txt");
            manager.addTask(task);
            manager.addEpic(epic);
            manager.addSubTask(subTask);
            manager.addSubTask(subTask1);
            System.out.println(manager.getSubtasksByEpicId(2));
            System.out.println(gson1.toJson(manager.getSubtasksByEpicId(2)).getBytes());
            //server1.stop();
        } catch(Exception e) {
            System.out.println("Exc.");
        }

    }
}
