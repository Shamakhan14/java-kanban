package com.yandexpraktikum.tasktracker.test;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.service.InMemoryTaskManager;
import com.yandexpraktikum.tasktracker.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {

    InMemoryTaskManager inMemoryTaskManager;
    Epic epic;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic("name", "description");
        inMemoryTaskManager.addEpic(epic);
    }

    @Test
    public void shouldBeNewWhenSubtaskListIsEmpty() {
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldBeNewWhenAllSubtasksAreNew() {
        SubTask subTask1 = new SubTask("SubTask1 name", "Subtask1 description", "NEW", epic.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        assertEquals(epic.getStatus(), Status.NEW);
    }

    @Test
    public void shouldBeDoneWhenAllSubtasksAreDone() {
        SubTask subTask1 = new SubTask("SubTask1 name", "Subtask1 description", "DONE", epic.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        assertEquals(epic.getStatus(), Status.DONE);
    }

    @Test
    public void shouldBeInProgressWhenSubtasksAreDoneAndNew() {
        SubTask subTask1 = new SubTask("SubTask1 name", "Subtask1 description", "NEW", epic.getId());
        SubTask subTask2 = new SubTask("SubTask2 name", "Subtask2 description", "DONE", epic.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }

    @Test
    public void shouldBeInProgressWhenSubtasksAreInProgress() {
        SubTask subTask1 = new SubTask("SubTask1 name", "Subtask1 description", "IN_PROGRESS", epic.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
    }
}