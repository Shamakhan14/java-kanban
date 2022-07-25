package com.yandexpraktikum.tasktracker;

import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.service.HTTPTaskManager;
import com.yandexpraktikum.tasktracker.service.KVServer;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            KVServer kvServer = new KVServer();
            kvServer.start();
            HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/", false);
            Task task = new Task("N", "D", "NEW");
            httpTaskManager.addTask(task);
            List<Task> list = httpTaskManager.getTasks();
            System.out.println(list.get(0).toString());
        } catch (IOException ioException) {
            System.out.println("Oops!");
        }

    }
}
