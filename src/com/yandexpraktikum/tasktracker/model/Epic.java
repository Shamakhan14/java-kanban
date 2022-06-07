package com.yandexpraktikum.tasktracker.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private List<Integer> subTaskIds;

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
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
