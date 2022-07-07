package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.Status;
import com.yandexpraktikum.tasktracker.util.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    protected String name;
    protected String description;
    protected Status status;
    protected int id;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, String status, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.now();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public String toString() {
        return id + "," + getTaskType().name() + "," + name + "," + status + "," + description + "," +
                getStartTime().toString() + "," + duration.toMinutes();
    }
}
