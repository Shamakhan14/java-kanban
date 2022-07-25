package com.yandexpraktikum.tasktracker.service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new Gson();
    private final HttpServer server;
    private TaskManager manager;

    public HttpTaskServer() throws IOException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubTaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", new TaskHandler());
        server.createContext("/tasks/epic", new EpicHandler());
        server.createContext("/tasks/subtask", new SubTaskHandler());
        server.createContext("/tasks/history", new HistoryHandler());
    }

    class TaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                String method = httpExchange.getRequestMethod();
                if (path.equals("/tasks/")) {
                    sendText(httpExchange, gson.toJson(manager.getSortedSet()));
                }
                switch (method) {
                    case "POST":
                        String body = readText(httpExchange);
                        Task task = gson.fromJson(body, Task.class);
                        boolean isUpdated = false;
                        for (Task task1 : manager.getTasks()) {
                            if (task1.getId() == task.getId()) {
                                manager.updateTask(task);
                                isUpdated = true;
                            }
                        }
                        if (!isUpdated) {
                            manager.addTask(task);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        if (path.endsWith("/task/")) {
                            sendText(httpExchange, gson.toJson(manager.getTasks()));
                        }
                        if (path.contains("/?id=")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            sendText(httpExchange, gson.toJson(manager.getTaskById(id)));
                        }
                        break;
                    case "DELETE":
                        if (path.endsWith("/task/")) {
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.clearAllTasks();
                        }
                        if (path.contains("/?id=")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.removeTaskById(id);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException ioException) {
                System.out.println("Ошибка сервера.\n" + ioException.getMessage());
            } finally {
                httpExchange.close();
            }
        }
    }

    class EpicHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            try {
                switch (method) {
                    case "POST":
                        String body = readText(httpExchange);
                        Epic epic = gson.fromJson(body, Epic.class);
                        boolean isUpdated = false;
                        for (Epic epic1 : manager.getEpics()) {
                            if (epic1.getId() == epic.getId()) {
                                manager.updateEpic(epic);
                                isUpdated = true;
                            }
                        }
                        if (!isUpdated) {
                            manager.addEpic(epic);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        if (path.endsWith("/epic/")) {
                            sendText(httpExchange, gson.toJson(manager.getEpics()));
                        }
                        if (path.contains("/?id=")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            sendText(httpExchange, gson.toJson(manager.getEpicById(id)));
                        }
                        break;
                    case "DELETE":
                        if (path.endsWith("/epic/")) {
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.clearAllEpics();
                        }
                        if (path.contains("/?id=")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.removeEpicById(id);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(404, 0);
                }
            } catch (IOException ioException) {
                System.out.println("Ошибка сервера.\n" + ioException.getMessage());
            } finally {
                httpExchange.close();
            }
        }
    }

    class SubTaskHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                String path = httpExchange.getRequestURI().getPath();
                String method = httpExchange.getRequestMethod();
                switch (method) {
                    case "POST":
                        String body = readText(httpExchange);
                        SubTask subTask = gson.fromJson(body, SubTask.class);
                        boolean isUpdated = false;
                        for (SubTask subTask1 : manager.getSubTasks()) {
                            if (subTask1.getId() == subTask.getId()) {
                                manager.updateSubTask(subTask);
                                isUpdated = true;
                            }
                        }
                        if (!isUpdated) {
                            manager.addSubTask(subTask);
                        }
                        httpExchange.sendResponseHeaders(201, 0);
                        break;
                    case "GET":
                        if (path.contains("epic")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            sendText(httpExchange, gson.toJson(manager.getSubtasksByEpicId(id)));
                        }
                        if (path.endsWith("/subtask/")) {
                            sendText(httpExchange, gson.toJson(manager.getSubTasks()));
                        }
                        if (path.contains("/?id=") && !path.contains("epic")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            sendText(httpExchange, gson.toJson(manager.getSubtaskById(id)));
                        }
                        break;
                    case "DELETE":
                        if (path.endsWith("/subtask/")) {
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.clearAllSubtasks();
                        }
                        if (path.contains("/?id=")) {
                            int id = Integer.parseInt(path.split("=")[1]);
                            httpExchange.sendResponseHeaders(200, 0);
                            manager.removeSubtaskById(id);
                        }
                        break;
                    default:
                        httpExchange.sendResponseHeaders(404, 0);
                }
                httpExchange.close();
            } catch (IOException ioException) {
                System.out.println("Ошибка сервера.\n" + ioException.getMessage());
            } finally {
                httpExchange.close();
            }
        }
    }

    class HistoryHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) {
            try {
                sendText(httpExchange, gson.toJson(manager.getHistory()));
            } catch (IOException ioException) {
                System.out.println("Ошибка сервера.\n" + ioException.getMessage());
            } finally {
                httpExchange.close();
            }
        }
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void stop() {
        server.stop(0);
    }

    public void start() {
        server.start();
    }

    public static void main(String[] args) {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            HttpTaskServer server1 = new HttpTaskServer();
            server1.start();
            Gson gson1 = new Gson();
            Task task = new Task("N", "D", "NEW");
            System.out.println(gson1.toJson(task));
            Epic epic = new Epic("N2", "D2");
            System.out.println(gson1.toJson(epic));
            SubTask subTask = new SubTask("N3", "D3", "NEW", 2);
            System.out.println(gson1.toJson(subTask));
            SubTask subTask1 = new SubTask("N4", "D4", "NEW", 2);
            System.out.println(gson1.toJson(subTask1));
            //server1.stop();
        } catch(Exception e) {
            System.out.println("Exc.");
        }

    }
}
