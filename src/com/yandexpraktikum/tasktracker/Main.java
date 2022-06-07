package com.yandexpraktikum.tasktracker;

import com.yandexpraktikum.tasktracker.model.*;
import com.yandexpraktikum.tasktracker.service.TaskManager;
import com.yandexpraktikum.tasktracker.util.Managers;

public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = Managers.getDefault();

        //Заполнение и вывод
        Task task1 = new Task("Уборка", "Убраться в комнате", "NEW");
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("Посуда", "Помыть посуду", "IN_PROGRESS");
        inMemoryTaskManager.addTask(task2);
        Epic epic1 = new Epic("Java", "Посаниматься Java");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Теория", "Теория", "IN_PROGRESS", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Практика", "Практика", "NEW", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("Зачет", "Зачет", "NEW", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask3);
        Epic epic2 = new Epic("Ужин", "Покушать");
        inMemoryTaskManager.addEpic(epic2);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());

        System.out.println("Запросы и вывод");
        System.out.println(inMemoryTaskManager.getTaskById(task1.getId()));
        System.out.println(inMemoryTaskManager.getTaskById(task2.getId()));
        System.out.println(inMemoryTaskManager.getEpicById(epic1.getId()));
        System.out.println(inMemoryTaskManager.getTaskById(task1.getId()));
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Удаление задачи и вывод");
        inMemoryTaskManager.removeTaskById(task1.getId());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Удаление эпика и вывод");
        inMemoryTaskManager.removeEpicById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}
