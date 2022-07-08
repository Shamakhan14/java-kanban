package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Counter;
import com.yandexpraktikum.tasktracker.util.Managers;
import com.yandexpraktikum.tasktracker.util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Map<Integer,Task> tasks;
    protected Map<Integer,Epic> epics;
    protected Map<Integer,SubTask> subTasks;
    protected TreeSet<Task> sortedSet;
    protected HistoryManager inMemoryHistoryManager = Managers.getDefaultHistory();
    protected Counter counter;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.counter = new Counter();
        this.sortedSet = new TreeSet<>((Task o1, Task o2) -> {
            if (o1.getStartTime().isAfter(o2.getStartTime())) return 1;
            if (o1.getStartTime().isBefore(o2.getStartTime())) return -1;
            return 0;
        });
    }

    /*Чтобы не было пересечений во времени, нужно, чтобы и начало, и конец первой задачи были до начала или после
    конца второй задачи. Эпики проверять и добавлять в сет бессмысленно, только таски и сабтаски.*/
    private boolean noTimeCollision(Task task1) {
        if (sortedSet.isEmpty()) return true;
        for (Task task2: sortedSet) {
            if ((task1.getStartTime().isBefore(task2.getStartTime()) &&
                    task1.getEndTime().isBefore(task2.getStartTime())) ||
                    (task1.getStartTime().isAfter(task2.getEndTime()) &&
                    task1.getEndTime().isAfter(task2.getEndTime()))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addTask (Task task) {
        if (noTimeCollision(task)) {
            task.setId(counter.getNewId());
            tasks.put(task.getId(), task);
            sortedSet.add(task);
        }
    }

    @Override
    public void addEpic (Epic epic) {
        epic.setId(counter.getNewId());
        epics.put(epic.getId(), epic);
    }

    protected void updateEpicTime(Epic epic) {
        List<Integer> epicSubTaskIds = epic.getSubTaskIds();
        LocalDateTime newStart = LocalDateTime.MAX;
        Duration newDuration = Duration.ZERO;
        LocalDateTime newEnd = LocalDateTime.MIN;
        for (Integer subTaskId: epicSubTaskIds) {
            SubTask subTask = subTasks.get(subTaskId);
            newDuration = newDuration.plus(subTask.getDuration());
            if (subTask.getStartTime().isBefore(newStart)) newStart = subTask.getStartTime();
            if (subTask.getEndTime().isAfter(newEnd)) newEnd = subTask.getEndTime();
        }
        epic.setStartTime(newStart);
        epic.setDuration(newDuration);
        epic.setEndTime(newEnd);
    }

    @Override
    public void addSubTask (SubTask subTask) {
        if (epics.containsKey(subTask.getEpicId()) && noTimeCollision(subTask)) {
            subTask.setId(counter.getNewId()); //присвоили ид
            Epic epic = epics.get(subTask.getEpicId()); //забрали эпик из мапа
            List<Integer> subTaskIds = epic.getSubTaskIds(); //забрали лист с сабами из эпика
            subTaskIds.add(subTask.getId()); //добавили в лист ид саба
            subTasks.put(subTask.getId(), subTask); //положили саб в мапу
            updateEpicStatus(epic);
            updateEpicTime(epic);
            sortedSet.add(subTask);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void clearAllTasks() {
        for (Integer id: tasks.keySet()) {
            inMemoryHistoryManager.remove(id);
            sortedSet.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        for (Integer id: epics.keySet()) {
            inMemoryHistoryManager.remove(id);
        }
        epics.clear();
        for (Integer id: subTasks.keySet()) {
            inMemoryHistoryManager.remove(id);
            sortedSet.remove(subTasks.get(id));
        }
        subTasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        for (Integer id: subTasks.keySet()) {
            inMemoryHistoryManager.remove(id);
            sortedSet.remove(subTasks.get(id));
        }
        subTasks.clear();
        for (Epic epic: epics.values()) {
            epic.getSubTaskIds().clear();
            epic.setStatus("NEW");
        }
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
            sortedSet.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) { //удаление эпика по ид
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer newId: epic.getSubTaskIds()) {
                inMemoryHistoryManager.remove(newId);
                sortedSet.remove(subTasks.get(newId));
                subTasks.remove(newId);
            }
            inMemoryHistoryManager.remove(id);
            epics.remove(id);
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
            updateEpicTime(newEpic);
            inMemoryHistoryManager.remove(id);
            sortedSet.remove(subTasks.get(id));
            subTasks.remove(id);
        }
    }

    @Override
    public List<SubTask> getSubtasksByEpicId(int id) { //вывод списка подзадач эпика
        List<SubTask> epicSubTasks = new ArrayList<>();
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Integer> subTaskIds = epic.getSubTaskIds();
            for (Integer newId : subTaskIds) {
                epicSubTasks.add(subTasks.get(newId));
            }
        }
        return epicSubTasks;
    }

    @Override
    public void updateTask(Task task) {
        /*сохраняем задачу, удаляем из сорт.списка, чтобы не учитывать в проверке по времени
        проверяем и меняем
        если не проходит по условиям, возвращаем задачу обратно в сорт.список*/
        if (tasks.containsKey(task.getId())) {
            Task savedTask = tasks.get(task.getId());
            sortedSet.remove(savedTask);
            if (noTimeCollision(task)) {
                tasks.put(task.getId(), task);
                sortedSet.add(task);
            } else {
                sortedSet.add(savedTask);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            SubTask savedSubTask = subTasks.get(subTask.getId());
            sortedSet.remove(savedSubTask);
            if (noTimeCollision(subTask)) {
                subTasks.put(subTask.getId(), subTask);
                Epic epic = epics.get(subTask.getEpicId());
                updateEpicStatus(epic);
                updateEpicTime(epic);
                sortedSet.add(subTask);
            } else {
                sortedSet.add(savedSubTask);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    protected void updateEpicStatus(Epic epic) {
        List<Integer> subTaskIds = epic.getSubTaskIds();
        if (subTaskIds.isEmpty()) {
            epic.setStatus("NEW");
        }
        boolean isNew = true;
        boolean isDone = true;
        for (Integer id: subTaskIds) {
            SubTask subTask = subTasks.get(id);
            if (!subTask.getStatus().equals(Status.NEW)) {
                isNew = false;
            }
            if (!subTask.getStatus().equals(Status.DONE)) {
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
