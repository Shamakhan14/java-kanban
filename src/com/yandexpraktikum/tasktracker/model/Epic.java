package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.TaskType;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private List<Integer> subTaskIds;
    private TaskType taskType = TaskType.EPIC;

    public Epic(String name, String description) {
        super(name, description, "NEW");
        this.subTaskIds = new ArrayList<>();
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public String toString() {
        return id + "," + taskType.name() + "," + name + "," + status + "," + description + ",";
    }
}
