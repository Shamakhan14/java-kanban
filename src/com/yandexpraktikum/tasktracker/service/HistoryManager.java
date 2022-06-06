package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.*;
import java.util.List;

public interface HistoryManager {

    void addTaskToHistory(Task task);
    void remove (int id);
    List<Task> getHistory();
}
