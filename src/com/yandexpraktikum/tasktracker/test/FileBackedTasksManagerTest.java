package com.yandexpraktikum.tasktracker.test;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private final File file = new File("saveTest.txt");

    @Override
    public FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(file);
    }

    @Test
    public void shouldSaveAndLoadEmptyManager() {
        taskManager.save();
        taskManager = FileBackedTasksManager.loadFromFile(file);
        FileBackedTasksManager.print(taskManager);
    }

    @Test
    public void shouldSaveAndLoadManagerWithEmptyHistory() {
        Epic epic = new Epic("name", "description");
        taskManager.addTask(epic);
        Epic epic1 = taskManager.getEpicById(epic.getId());
        taskManager.save();
        taskManager = FileBackedTasksManager.loadFromFile(file);
        FileBackedTasksManager.print(taskManager);
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
        taskManager.save();
        taskManager = FileBackedTasksManager.loadFromFile(file);
        FileBackedTasksManager.print(taskManager);
    }
}
