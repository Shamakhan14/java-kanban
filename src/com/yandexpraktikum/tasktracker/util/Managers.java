package com.yandexpraktikum.tasktracker.util;

import com.yandexpraktikum.tasktracker.service.*;

import java.io.File;

public class Managers {

    private Managers() {}

    public static TaskManager getDefault(String url) {
        return new HTTPTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager() {
        return new FileBackedTasksManager("save.txt");
    }
}
