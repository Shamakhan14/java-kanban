package com.yandexpraktikum.tasktracker.test;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    abstract T createTaskManager();

    @BeforeEach
    private void updateTaskManager() {
        taskManager = createTaskManager();
    }

    @Test
    public void addAndGetTask() {
        Task task = new Task("name", "description", "NEW");
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    public void addAndGetEpic() {
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    public void addAndGetSubtask() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask name", "SubTask description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        final int subTaskId = subTask.getId();

        final SubTask savedSubTask = taskManager.getSubtaskById(subTaskId);

        assertNotNull(savedSubTask, "Сабтаск не найден.");
        assertEquals(subTask, savedSubTask, "Сабтаски не совпадают.");

        final List<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Сабтаски на возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество сабтасков.");
        assertEquals(subTask, subTasks.get(0), "Сабтаски не совпадают.");
    }

    @Test
    public void shouldBeEmptyTasksList() {
        final List<Task> emptyTasks = taskManager.getTasks();
        assertEquals(emptyTasks.size(), 0);
    }

    @Test
    public void shouldBeEmptyEpicsList() {
        final List<Epic> emptyEpics = taskManager.getEpics();
        assertEquals(emptyEpics.size(), 0);
    }

    @Test
    public void shouldBeEmptySubTasksList() {
        final List<SubTask> emptySubTasks = taskManager.getSubTasks();
        assertEquals(emptySubTasks.size(), 0);
    }

    @Test
    public void shouldClearTasks() {
        Task task = new Task("name", "description", "NEW");
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTasks().size());
        taskManager.clearAllTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    public void shouldClearEpics() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Sub name", "Sub description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubTasks().size());
        taskManager.clearAllEpics();
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    public void shouldClearSubTasks() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Sub name", "Sub description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubTasks().size());
        assertEquals(subTask.getId(), epic.getSubTaskIds().get(0));
        taskManager.clearAllSubtasks();
        assertEquals(0, taskManager.getSubTasks().size());
        assertEquals(0, epic.getSubTaskIds().size());
    }

    @Test
    public void shouldReturnTaskOrNull() {
        assertEquals(null, taskManager.getTaskById(-1));
        Task task = new Task("name", "description", "NEW");
        taskManager.addTask(task);
        final int taskId = task.getId();
        assertEquals(task, taskManager.getTaskById(taskId));
    }

    @Test
    public void shouldReturnEpicOrNull() {
        assertEquals(null, taskManager.getEpicById(-1));
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        assertEquals(epic, taskManager.getEpicById(epicId));
    }

    @Test
    public void shouldReturnSubtaskOrNull() {
        assertEquals(null, taskManager.getSubtaskById(-1));
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("SubTask name", "SubTask description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        final int subTaskId = subTask.getId();
        assertEquals(subTask, taskManager.getSubtaskById(subTaskId));
    }

    @Test
    public void shouldRemoveTaskIfPresent() {
        taskManager.removeTaskById(-1);
        Task task1 = new Task("name1", "description1", "NEW");
        taskManager.addTask(task1);
        Task task2 = new Task("name2", "description2", "NEW");
        taskManager.addTask(task2);
        assertEquals(2, taskManager.getTasks().size());
        taskManager.removeTaskById(task1.getId());
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    public void shouldRemoveEpicIfPresent() {
        taskManager.removeEpicById(-1);
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Sub name", "Sub description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubTasks().size());
        taskManager.removeEpicById(epic.getId());
        assertEquals(0, taskManager.getEpics().size());
        assertEquals(0, taskManager.getSubTasks().size());
    }

    @Test
    public void shouldRemoveSubTaskIfPresent() {
        taskManager.removeSubtaskById(-1);
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Sub name", "Sub description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(1, taskManager.getSubTasks().size());
        assertEquals(subTask.getId(), epic.getSubTaskIds().get(0));
        taskManager.removeSubtaskById(subTask.getId());
        assertEquals(0, taskManager.getSubTasks().size());
        assertEquals(0, epic.getSubTaskIds().size());
    }

    @Test
    public void shouldReturnEpicsSubtaskList() {
        final List<SubTask> subTasksEmpty = taskManager.getSubtasksByEpicId(-1);
        assertEquals(0, subTasksEmpty.size());
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Sub name", "Sub description", "NEW", epic.getId());
        taskManager.addSubTask(subTask);
        final List<SubTask> subTasks = taskManager.getSubtasksByEpicId(epic.getId());
        assertEquals(1, subTasks.size());
        assertEquals(subTask, subTasks.get(0));

    }

    @Test
    public void shouldUpdateTask() {
        Task task1 = new Task("name1", "description1", "NEW");
        taskManager.updateTask(task1);
        taskManager.addTask(task1);
        Task task2 = new Task("name2", "description2", "NEW");
        task2.setId(task1.getId());
        taskManager.updateTask(task2);
        assertEquals("name2", taskManager.getTasks().get(0).getName());
    }

    @Test
    public void shouldUpdateEpic() {
        Epic epic1 = new Epic("name1", "description1");
        taskManager.updateEpic(epic1);
        taskManager.addEpic(epic1);
        Epic epic2 = new Epic("name2", "description2");
        epic2.setId(epic1.getId());
        taskManager.updateEpic(epic2);
        assertEquals("name2", taskManager.getEpics().get(0).getName());
    }

    @Test
    public void shouldUpdateSubTask() {
        Epic epic = new Epic("Epic name", "Epic description");
        taskManager.addEpic(epic);
        SubTask subTask1 = new SubTask("name1", "description1", "NEW", epic.getId());
        taskManager.updateSubTask(subTask1);
        taskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("name2", "description2", "NEW", epic.getId());
        subTask2.setId(subTask1.getId());
        taskManager.updateSubTask(subTask2);
        assertEquals("name2", taskManager.getSubTasks().get(0).getName());
    }

    @Test
    public void shouldReturnHistory() {
        assertEquals(0, taskManager.getHistory().size());
        Task task = new Task("name", "description", "NEW");
        taskManager.addTask(task);
        Task newTask1 = taskManager.getTaskById(task.getId());
        Task newTask2 = taskManager.getTaskById(task.getId());
        assertEquals(1, taskManager.getHistory().size());
        assertEquals(task, taskManager.getHistory().get(0));
        taskManager.clearAllTasks();
        assertEquals(0, taskManager.getHistory().size());
    }
}
