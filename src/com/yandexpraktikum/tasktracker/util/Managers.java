package com.yandexpraktikum.tasktracker.util;

import com.yandexpraktikum.tasktracker.service.HistoryManager;
import com.yandexpraktikum.tasktracker.service.InMemoryHistoryManager;
import com.yandexpraktikum.tasktracker.service.InMemoryTaskManager;
import com.yandexpraktikum.tasktracker.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        return inMemoryTaskManager;
    }

    public static HistoryManager getDefaultHistory() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        return inMemoryHistoryManager;
    }
}
