package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.*;

import java.util.ArrayList;

public interface TaskManager {

    void addTask (Task task);
    void addEpic (Epic epic);
    void addSubTask (SubTask subTask);

    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<SubTask> getSubTasks();

    void clearAllTasks();
    void clearAllEpics();
    void clearAllSubtasks();

    Task getTaskById(int id);
    Epic getEpicById(int id);
    SubTask getSubtaskById(int id);

    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(int id);

    ArrayList<SubTask> getSubtasksByEpicId(int id);

    void updateTask(Task task);
    void updateSubTask(SubTask subTask);
    void updateEpic(Epic epic);


}
