package com.yandexpraktikum.tasktracker.test;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.HTTPTaskManager;
import com.yandexpraktikum.tasktracker.service.KVServer;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest{
    private static final String URL = "http://localhost:8078/";
    private KVServer kvServer;
    private HTTPTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = new HTTPTaskManager(URL, false);
        } catch (IOException ioException) {
            System.out.println("Server start failed.");
        }
    }

    @AfterEach
    private void afterEach() {
        kvServer.stop();
    }

    @Test
    public void shouldSaveAndLoadManagerWithEmptyHistory() {
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        Epic epic1 = taskManager.getEpicById(epic.getId());
        HTTPTaskManager loadedTaskManager = new HTTPTaskManager(URL, true);
        assertEquals(taskManager.getTasks(), loadedTaskManager.getTasks());
        assertEquals(taskManager.getEpics(), loadedTaskManager.getEpics());
        assertEquals(taskManager.getSubTasks(), loadedTaskManager.getSubTasks());
        assertEquals(taskManager.getSortedSet(), loadedTaskManager.getSortedSet());
        assertEquals(taskManager.getHistory(), loadedTaskManager.getHistory());
    }

    @Test
    public void shouldSaveAndLoadFullManager() {
        Task task = new Task("Task name", "Task description", "NEW");
        taskManager.addTask(task);
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Subtask name", "Subtask description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        Task newTask = taskManager.getTaskById((task.getId()));
        SubTask newSubTask = taskManager.getSubtaskById(subTask.getId());
        HTTPTaskManager loadedTaskManager = new HTTPTaskManager(URL, true);
        assertEquals(taskManager.getTasks(), loadedTaskManager.getTasks());
        assertEquals(taskManager.getEpics(), loadedTaskManager.getEpics());
        assertEquals(taskManager.getSubTasks(), loadedTaskManager.getSubTasks());
        assertEquals(taskManager.getSortedSet(), loadedTaskManager.getSortedSet());
        assertEquals(taskManager.getHistory(), loadedTaskManager.getHistory());
    }
}
