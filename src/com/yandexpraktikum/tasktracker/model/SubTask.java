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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Task)) {
            return false;
        }
        SubTask subTask = (SubTask) o;
        return subTask.name.equals(name) &&
                subTask.description.equals(description) &&
                subTask.status == status &&
                subTask.id == id &&
                subTask.duration.equals(duration) &&
                subTask.startTime.equals(startTime) &&
                subTask.epicId == epicId;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31  * result + name.hashCode();
        result = 31  * result + description.hashCode();
        result = 31  * result + status.hashCode();
        result = 31  * result + id;
        result = 31  * result + duration.hashCode();
        result = 31  * result + startTime.hashCode();
        result = 31  * result + epicId;
        return result;
    }
}
