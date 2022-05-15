package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.Status;

public class Task {

    protected String name;
    protected String description;
    protected Status status;
    protected int id;

    public Task(String name, String description, String status) {
        this.name = name;
        this.description = description;
        this.status = Status.valueOf(status);
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
