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
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer,Task> tasks;
    protected Map<Integer,Epic> epics;
    protected Map<Integer,SubTask> subTasks;
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected Counter counter;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.counter = new Counter();
    }

    @Override
    public void addTask (Task task) { //добавить задачу
        task.setId(counter.getNewId());
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic (Epic epic) { //добавить эпик
        epic.setId(counter.getNewId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubTask (SubTask subTask) { //добавить подзадачу
        if (epics.containsKey(subTask.getEpicId())) {
            subTask.setId(counter.getNewId()); //присвоили ид
            Epic newEpic = epics.get(subTask.getEpicId()); //забрали эпик из мапа
            List<Integer> newSubTaskIds = newEpic.getSubTaskIds(); //забрали лист с сабами из эпика
            newSubTaskIds.add(subTask.getId()); //добавили в лист ид саба
            subTasks.put(subTask.getId(), subTask); //положили саб в мапу
            updateEpicStatus(newEpic);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    @Override
    public List<Task> getTasks() { //вывод списка задач
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() { //вывод списка эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() { //вывод списка подзадач
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllTasks() { //удалить все задачи
        for (Integer id: tasks.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        tasks.clear();
        System.out.println("Все задачи удалены.");
    }

    @Override
    public void clearAllEpics() { //удалить все эпики
        for (Integer id: epics.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        epics.clear();
        for (Integer id: subTasks.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        subTasks.clear();
        System.out.println("Все эпики и подзадачи удалены.");
    }

    @Override
    public void clearAllSubtasks() { //удалить все подзадачи
        for (Integer id: subTasks.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
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
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.containsKey(id)) {
            inMemoryHistoryManager.addTaskToHistory(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubtaskById(int id) {
        if (subTasks.containsKey(id)) {
            inMemoryHistoryManager.addTaskToHistory(subTasks.get(id));
            return subTasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void removeTaskById(int id) {  //удаление задачи по ИД
        if (tasks.containsKey(id)) {
            inMemoryHistoryManager.remove(id);
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
                inMemoryHistoryManager.remove(newId);
                subTasks.remove(newId);
            }
            inMemoryHistoryManager.remove(id);
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
            List<Integer> newSubTaskIds = newEpic.getSubTaskIds();
            newSubTaskIds.remove((Integer) newSubTask.getId());
            updateEpicStatus(newEpic);
            inMemoryHistoryManager.remove(id);
            subTasks.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    @Override
    public List<SubTask> getSubtasksByEpicId(int id) { //вывод списка подзадач эпика
        List<SubTask> newSubTasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic newEpic = epics.get(id);
            List<Integer> newSubTaskIds = newEpic.getSubTaskIds();
            for (Integer newId : newSubTaskIds) {
                newSubTasks.add(subTasks.get(newId));
            }
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

    protected void updateEpicStatus(Epic epic) {
        List<Integer> newSubTaskIds = epic.getSubTaskIds();
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
