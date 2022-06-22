package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.TaskType;

public class SubTask extends Task{

    private int epicId;
    private TaskType taskType = TaskType.SUBTASK;

    public SubTask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," + taskType.name() + "," + name + "," + status + "," + description + "," + epicId + ",";
    }
}
