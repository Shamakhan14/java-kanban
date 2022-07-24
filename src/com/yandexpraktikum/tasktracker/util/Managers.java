package com.yandexpraktikum.tasktracker.util;

import com.yandexpraktikum.tasktracker.service.*;

import java.io.File;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault() {
        return new HTTPTaskManager("http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager("save.txt");
    }
}
