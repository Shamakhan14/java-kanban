package com.yandexpraktikum.tasktracker.test;

import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class HistoryManagerTest {

    private InMemoryHistoryManager inMemoryHistoryManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldReturnEmptyHistory() {
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    public void shouldAddTaskToHistoryWODoubles() {
        Task task = new Task("name", "description", "NEW");
        inMemoryHistoryManager.addTaskToHistory(task);
        inMemoryHistoryManager.addTaskToHistory(task);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    public void shouldRemoveTaskFromTheBeginning() {
        Task task1 = new Task("name1", "description1", "NEW");
        task1.setId(0);
        Task task2 = new Task("name2", "description2", "NEW");
        task2.setId(1);
        Task task3 = new Task("name3", "description3", "NEW");
        task3.setId(2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task3);
        inMemoryHistoryManager.remove(0);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    public void shouldRemoveTaskFromInTheMiddle() {
        Task task1 = new Task("name1", "description1", "NEW");
        task1.setId(0);
        Task task2 = new Task("name2", "description2", "NEW");
        task2.setId(1);
        Task task3 = new Task("name3", "description3", "NEW");
        task3.setId(2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task3);
        inMemoryHistoryManager.remove(1);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    public void shouldRemoveTaskFromTheEnd() {
        Task task1 = new Task("name1", "description1", "NEW");
        task1.setId(0);
        Task task2 = new Task("name2", "description2", "NEW");
        task2.setId(1);
        Task task3 = new Task("name3", "description3", "NEW");
        task3.setId(2);
        inMemoryHistoryManager.addTaskToHistory(task1);
        inMemoryHistoryManager.addTaskToHistory(task2);
        inMemoryHistoryManager.addTaskToHistory(task3);
        inMemoryHistoryManager.remove(2);
        List<Task> history = inMemoryHistoryManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }
}
