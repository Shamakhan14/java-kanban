package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Counter;
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private HashMap<Integer,Task> tasks;
    private HashMap<Integer,Epic> epics;
    private HashMap<Integer,SubTask> subTasks;

    public Manager() {
        this.tasks = new HashMap<>();
        this.epics= new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    public void addTask (Task task) { //добавить задачу
        task.setId(Counter.getNewId());
        tasks.put(task.getId(), task);
    }

    public void addEpic (Epic epic) { //добавить эпик
        epic.setId(Counter.getNewId());
        epics.put(epic.getId(), epic);
    }

    public void addSubTask (SubTask subTask) { //добавить подзадачу
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(Counter.getNewId()); //присвоили ид
            Epic newEpic = epics.get(subTask.getEpicId()); //забрали эпик из мапа
            ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds(); //забрали лист с сабами из эпика
            newSubTaskIds.add(subTask.getId()); //добавили в лист ид саба
            newEpic.setSubTaskIds(newSubTaskIds); //положили обратно лист
            subTasks.put(subTask.getId(), subTask); //положили саб в мапу
            newEpic = updateEpicStatus(newEpic);
            epics.put(newEpic.getId(), newEpic); //положили обратно эпик
            //где-то здесь же лежит анекдот про дамочку с сумочкой
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    public ArrayList<Task> getTasks() { //вывод списка задач
        ArrayList<Task> newTasks = new ArrayList<>();
        for (Task task: tasks.values()) {
            newTasks.add(task);
        }
        return newTasks;
    }

    public ArrayList<Epic> getEpics() { //вывод списка эпиков
        ArrayList<Epic> newEpics = new ArrayList<>();
        for (Epic epic: epics.values()) {
            newEpics.add(epic);
        }
        return newEpics;
    }

    public ArrayList<SubTask> getSubTasks() { //вывод списка подзадач
        ArrayList<SubTask> newSubTasks = new ArrayList<>();
        for (SubTask subTask: subTasks.values()) {
            newSubTasks.add(subTask);
        }
        return newSubTasks;
    }

    public void clearAllTasks() { //удалить все задачи
        tasks.clear();
        System.out.println("Все задачи удалены.");
    }

    public void clearAllEpics() { //удалить все эпики
        epics.clear();
        subTasks.clear();
        System.out.println("Все эпики и подзадачи удалены.");
    }

    public void clearAllSubtasks() { //удалить все подзадачи
        subTasks.clear();
        for (Epic epic: epics.values()) {
            ArrayList<Integer> newSubTaskIds = new ArrayList<>();
            epic.setSubTaskIds(newSubTaskIds);
            epic.setStatus("NEW");
        }
        System.out.println("Все подзадачи удалены.");
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Такой задачи нет.");
            return null;
        }
    }

    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Такого эпика нет.");
            return null;
        }
    }

    public SubTask getSubtaskById(int id) {
        if (subTasks.containsKey(id)) {
            return subTasks.get(id);
        } else {
            System.out.println("Такой подзадачи нет.");
            return null;
        }
    }

    public void removeTaskById(int id) {  //удаление задачи по ИД
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    public void removeEpicById(int id) { //удаление эпика по ид
        if (epics.containsKey(id)) {
            Epic newEpic = epics.get(id);
            for (Integer newId: newEpic.getSubTaskIds()) {
                subTasks.remove(newId);
            }
            epics.remove(id);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    public void removeSubtaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask newSubTask = subTasks.get(id); //вытаскиваем саб
            Epic newEpic = epics.get(newSubTask.getEpicId()); //вытаскиваем эпик по сабу
            ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds(); //вытаскиваем список сабов эпика
            newSubTaskIds.remove(newSubTask.getId()); //удаляем ид саба
            newEpic.setSubTaskIds(newSubTaskIds); //обновляем список сабов
            newEpic = updateEpicStatus(newEpic);
            epics.put(newEpic.getId(), newEpic); //кладем эпик обратно
            subTasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    public ArrayList<SubTask> getSubtasksByEpicId(int id) { //вывод списка подзадач эпика
        ArrayList<SubTask> newSubTasks = new ArrayList<>();
        Epic newEpic = epics.get(id);
        ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds();
        for (Integer newId: newSubTaskIds) {
            newSubTasks.add(subTasks.get(newId));
        }
        return newSubTasks;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Искомая задача отсутствует.");
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            epic = updateEpicStatus(epic);
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Искомая подзадача отсутствует.");
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Искомый эпик отсутствует.");
        }
    }

    private Epic updateEpicStatus(Epic epic) {
        ArrayList<Integer> newSubTaskIds = epic.getSubTaskIds();
        if (newSubTaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return epic;
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Integer id: newSubTaskIds) {
            SubTask newSubTask = subTasks.get(id);
            if (!newSubTask.getStatus().equals("NEW")) {
                isNew = false;
            }
            if (!newSubTask.getStatus().equals("DONE")) {
                isDone = false;
            }
        }
        if (isNew) {
            epic.setStatus("NEW");
            return epic;
        } else if (isDone) {
            epic.setStatus("DONE");
            return epic;
        } else {
            epic.setStatus("IN_PROGRESS");
            return epic;
        }
    }
}
