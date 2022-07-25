package com.yandexpraktikum.tasktracker.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private Gson gson;

    public HTTPTaskManager(String url) {
        super("save.txt");
        client = new KVTaskClient(url);
        gson = new Gson();
    }

    public HTTPTaskManager(HTTPTaskManager manager) {
        super("save.txt");
        this.client = manager.client;
        gson = new Gson();
        load();
    }

    @Override
    public void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
        client.put("epics", jsonEpics);
        String jsonSubTasks = gson.toJson(new ArrayList<>(subTasks.values()));
        client.put("subtasks", jsonSubTasks);
        String jsonHistory = gson.toJson(getHistory().stream().map(Task::getId).collect(Collectors.toList()));
        client.put("history", jsonHistory);
    }

    private void load() {
        int maxID = 0;
        ArrayList<Task> taskList = gson.fromJson(client.load("tasks"), new TypeToken<ArrayList<Task>>(){}.getType());
        for (Task task: taskList) {
            tasks.put(task.getId(), task);
            if (task.getId() > maxID) maxID = task.getId();
            sortedSet.add(task);
        }
        ArrayList<Epic> epicList = gson.fromJson(client.load("epics"), new TypeToken<ArrayList<Epic>>(){}.getType());
        for (Epic epic: epicList) {
            epics.put(epic.getId(), epic);
            if (epic.getId() > maxID) maxID = epic.getId();
        }
        ArrayList<SubTask> subTaskList = gson.fromJson(client.load("subtasks"), new TypeToken<ArrayList<SubTask>>(){}.getType());
        for (SubTask subTask: subTaskList) {
            subTasks.put(subTask.getId(), subTask);
            if (subTask.getId() > maxID) maxID = subTask.getId();
            sortedSet.add(subTask);
        }
        counter.setId(maxID);
        ArrayList<Integer> historyList = gson.fromJson(client.load("history"), new TypeToken<ArrayList<Integer>>(){}.getType());
        for (Integer id: historyList) {
            if (tasks.containsKey(id)) {
                inMemoryHistoryManager.addTaskToHistory(tasks.get(id));
                continue;
            }
            if (epics.containsKey(id)) {
                inMemoryHistoryManager.addTaskToHistory(epics.get(id));
                continue;
            }
            if (subTasks.containsKey(id)) {
                inMemoryHistoryManager.addTaskToHistory(subTasks.get(id));
            }
        }
    }
}
