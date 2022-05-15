package com.yandexpraktikum.tasktracker.util;

import com.yandexpraktikum.tasktracker.service.InMemoryTaskManager;
import com.yandexpraktikum.tasktracker.service.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        return inMemoryTaskManager;
    }
}
