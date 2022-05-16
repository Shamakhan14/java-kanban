package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Counter;
import com.yandexpraktikum.tasktracker.util.Managers;
import com.yandexpraktikum.tasktracker.util.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private HashMap<Integer,Task> tasks;
    private HashMap<Integer,Epic> epics;
    private HashMap<Integer,SubTask> subTasks;
    public HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics= new HashMap<>();
        this.subTasks = new HashMap<>();
    }

    @Override
    public void addTask (Task task) { //добавить задачу
        task.setId(Counter.getNewId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic (Epic epic) { //добавить эпик
        epic.setId(Counter.getNewId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask (SubTask subTask) { //добавить подзадачу
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(Counter.getNewId()); //присвоили ид
            Epic newEpic = epics.get(subTask.getEpicId()); //забрали эпик из мапа
            ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds(); //забрали лист с сабами из эпика
            newSubTaskIds.add(subTask.getId()); //добавили в лист ид саба
            subTasks.put(subTask.getId(), subTask); //положили саб в мапу
            updateEpicStatus(newEpic);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    @Override
    public ArrayList<Task> getTasks() { //вывод списка задач
        ArrayList<Task> newTasks = new ArrayList<>(tasks.values());
        return newTasks;
    }

    @Override
    public ArrayList<Epic> getEpics() { //вывод списка эпиков
        ArrayList<Epic> newEpics = new ArrayList<>(epics.values());
        return newEpics;
    }

    @Override
    public ArrayList<SubTask> getSubTasks() { //вывод списка подзадач
        ArrayList<SubTask> newSubTasks = new ArrayList<>(subTasks.values());
        return newSubTasks;
    }

    @Override
    public void clearAllTasks() { //удалить все задачи
        tasks.clear();
        System.out.println("Все задачи удалены.");
    }

    @Override
    public void clearAllEpics() { //удалить все эпики
        epics.clear();
        subTasks.clear();
        System.out.println("Все эпики и подзадачи удалены.");
    }

    @Override
    public void clearAllSubtasks() { //удалить все подзадачи
        subTasks.clear();
        for (Epic epic: epics.values()) {
            epic.getSubTaskIds().clear();
            epic.setStatus("NEW");
        }
        System.out.println("Все подзадачи удалены.");
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.addTaskToHistory(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Такой задачи нет.");
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            inMemoryHistoryManager.addTaskToHistory(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Такого эпика нет.");
            return null;
        }
    }

    @Override
    public SubTask getSubtaskById(int id) {
        if (subTasks.containsKey(id)) {
            inMemoryHistoryManager.addTaskToHistory(subTasks.get(id));
            return subTasks.get(id);
        } else {
            System.out.println("Такой подзадачи нет.");
            return null;
        }
    }

    @Override
    public void removeTaskById(int id) {  //удаление задачи по ИД
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    @Override
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

    @Override
    public void removeSubtaskById(int id) {
        if (subTasks.containsKey(id)) {
            SubTask newSubTask = subTasks.get(id);
            Epic newEpic = epics.get(newSubTask.getEpicId());
            ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds();
            newSubTaskIds.remove((Integer) newSubTask.getId());
            updateEpicStatus(newEpic);
            subTasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public ArrayList<SubTask> getSubtasksByEpicId(int id) { //вывод списка подзадач эпика
        ArrayList<SubTask> newSubTasks = new ArrayList<>();
        Epic newEpic = epics.get(id);
        ArrayList<Integer> newSubTaskIds = newEpic.getSubTaskIds();
        for (Integer newId: newSubTaskIds) {
            newSubTasks.add(subTasks.get(newId));
        }
        return newSubTasks;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Искомая задача отсутствует.");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            Epic epic = epics.get(subTask.getEpicId());
            updateEpicStatus(epic);
        } else {
            System.out.println("Искомая подзадача отсутствует.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        } else {
            System.out.println("Искомый эпик отсутствует.");
        }
    }

    private void updateEpicStatus(Epic epic) {
        ArrayList<Integer> newSubTaskIds = epic.getSubTaskIds();
        if (newSubTaskIds.isEmpty()) {
            epic.setStatus("NEW");
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Integer id: newSubTaskIds) {
            SubTask newSubTask = subTasks.get(id);
            if (!newSubTask.getStatus().equals(Status.NEW)) {
                isNew = false;
            }
            if (!newSubTask.getStatus().equals(Status.DONE)) {
                isDone = false;
            }
        }
        if (isNew) {
            epic.setStatus("NEW");
        } else if (isDone) {
            epic.setStatus("DONE");
        } else {
            epic.setStatus("IN_PROGRESS");
        }
    }

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryManager.getHistory();
    }
}
