package com.yandexpraktikum.tasktracker.util;

import com.yandexpraktikum.tasktracker.service.HistoryManager;
import com.yandexpraktikum.tasktracker.service.InMemoryHistoryManager;
import com.yandexpraktikum.tasktracker.service.InMemoryTaskManager;
import com.yandexpraktikum.tasktracker.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
