package com.yandexpraktikum.tasktracker;

import com.yandexpraktikum.tasktracker.model.*;
import com.yandexpraktikum.tasktracker.service.Manager;

public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();

        Epic epic1 = new Epic("Помыть посуду", "Взять губку и помыть посуду");
        manager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Взять губку",
                "Найти губку и взять в руки", "NEW", epic1.getId());
        SubTask subTask2 = new SubTask("Взять моющее средство",
                "Найти средство и достать его", "NEW", epic1.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        Epic epic2 = new Epic("Пропылесосить квартиру",
                "Откопать пылесос и пропылесосить");
        manager.addEpic(epic2);
        SubTask subTask3 = new SubTask("Распутить шнур",
                "Распутать шнур пылесоса", "NEW", epic2.getId());
        manager.addSubTask(subTask3);

        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        subTask1.setName("Откопать губку");
        subTask1.setStatus("IN_PROGRESS");
        manager.updateSubTask(subTask1);
        subTask3.setStatus("DONE");
        manager.updateSubTask(subTask3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());

        manager.removeEpicById(epic2.getId());
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
    }
}
