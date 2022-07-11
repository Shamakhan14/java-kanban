package com.yandexpraktikum.tasktracker.model;

import com.yandexpraktikum.tasktracker.util.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{

    private List<Integer> subTaskIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, "NEW", 0, LocalDateTime.now());
        this.subTaskIds = new ArrayList<>();
        this.endTime = LocalDateTime.now().plusNanos(1);
    }

    public Epic(String name, String description, String status, int duration, LocalDateTime startTime) {
        super(name, description, "NEW", duration, startTime);
        this.subTaskIds = new ArrayList<>();
        this.endTime = LocalDateTime.now().plusNanos(1);
    }

    public List<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void setSubTaskIds(ArrayList<Integer> subTaskIds) {
        this.subTaskIds = subTaskIds;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return id + "," + getTaskType().name() + "," + name + "," + status + "," + description + "," +
                getStartTime().toString() + "," + duration.toMinutes();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Task)) {
            return false;
        }
        Epic epic = (Epic) o;
        return epic.name.equals(name) &&
                epic.description.equals(description) &&
                epic.status == status &&
                epic.id == id &&
                epic.duration.equals(duration) &&
                epic.startTime.equals(startTime) &&
                epic.subTaskIds.equals(subTaskIds);
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
        result = 31 * result + subTaskIds.hashCode();
        return result;
    }
}
