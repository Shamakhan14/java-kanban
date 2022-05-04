package com.yandexpraktikum.tasktracker.model;

public class SubTask extends Task{

    private int epicId;

    public SubTask(String name, String description, String status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        this.id = 0;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
