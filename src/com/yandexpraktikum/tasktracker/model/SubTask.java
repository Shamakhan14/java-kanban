package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.TaskType;

import java.time.LocalDateTime;

public class SubTask extends Task{

    private int epicId;

    public SubTask(String name, String description, String status, int duration, LocalDateTime startTime, int epicId) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return id + "," + getTaskType().name() + "," + name + "," + status + "," + description + "," +
                getStartTime().toString() + "," + duration.toMinutes() + "," + epicId;
    }
}
