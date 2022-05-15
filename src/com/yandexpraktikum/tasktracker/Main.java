package com.yandexpraktikum.tasktracker;

import com.yandexpraktikum.tasktracker.model.*;
import com.yandexpraktikum.tasktracker.service.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Epic epic1 = new Epic("Помыть посуду", "Взять губку и помыть посуду");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Взять губку",
                "Найти губку и взять в руки", "NEW", epic1.getId());
        SubTask subTask2 = new SubTask("Взять моющее средство",
                "Найти средство и достать его", "NEW", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        inMemoryTaskManager.addSubTask(subTask2);
        Epic epic2 = new Epic("Пропылесосить квартиру",
                "Откопать пылесос и пропылесосить");
        inMemoryTaskManager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Распутить шнур",
                "Распутать шнур пылесоса", "NEW", epic2.getId());
        inMemoryTaskManager.addSubTask(subTask3);

        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());

        subTask1.setName("Откопать губку");
        subTask1.setStatus("IN_PROGRESS");
        inMemoryTaskManager.updateSubTask(subTask1);
        subTask3.setStatus("DONE");
        inMemoryTaskManager.updateSubTask(subTask3);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());

        inMemoryTaskManager.removeEpicById(epic2.getId());
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());
    }
}
