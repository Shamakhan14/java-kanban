package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.TaskType;

import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {

    private KVTaskClient kvTaskClient;
    private String token;
    private static final String HEADLINE = "id,type,name,status,description,startTime,duration,epicId";

    public HTTPTaskManager(String url) {
        super(url);
        kvTaskClient = new KVTaskClient(url);
        token = kvTaskClient.getToken();
    }

    @Override
    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append(HEADLINE + "\n");
        for (Task task: tasks.values()) {
            sb.append(task.toString() + "\n");
        }
        for (Epic epic: epics.values()) {
            sb.append(epic.toString() + "\n");
        }
        for (SubTask subTask: subTasks.values()) {
            sb.append(subTask.toString() + "\n");
        }
        sb.append("\n");
        if (!getHistory().isEmpty()) {
            StringBuilder line = new StringBuilder();
            for (Task task: getHistory()) {
                line.append(task.getId() + ",");
            }
            line.deleteCharAt(line.length()-1);
            sb.append(line);
        }
        kvTaskClient.put(token, sb.toString());
    }

    public static HTTPTaskManager load(String url, String key) {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager(url);
        String content = httpTaskManager.kvTaskClient.load(key);
        if (!content.isEmpty()) {
            String[] lines = content.split("\n");
            if (lines.length >= 2) { //проверка на пустоту заполнения файла
                int newId = 0;
                int lineNum; //номер строки, до которой идем в циклах, зависит от наличии истории
                if (lines[lines.length-2].isBlank()) { //проверка на присутствие истории
                    lineNum = lines.length-2; //история есть, идем до предпоследней строки
                } else {
                    lineNum = lines.length; //истории нет, идем до конца файла
                }
                for (int i = 1; i < lineNum; i++) {
                    int id = Integer.parseInt(lines[i].substring(0, 1));
                    if (id > newId) {
                        newId = id;
                    }
                }
                httpTaskManager.counter.setId(newId);
                for (int i = 1; i < lineNum; i++) {
                    Task task = FileBackedTasksManager.fromString(lines[i]);
                    String[] line = lines[i].split(",");
                    switch (TaskType.valueOf(line[1])) {
                        case TASK:
                            httpTaskManager.tasks.put(task.getId(), task);
                            httpTaskManager.sortedSet.add(task);
                            break;
                        case EPIC:
                            httpTaskManager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            httpTaskManager.subTasks.put(task.getId(), (SubTask) task);
                            httpTaskManager.sortedSet.add(task);
                            Epic epic = httpTaskManager.epics.get(((SubTask) task).getEpicId());
                            List<Integer> subTaskIds = epic.getSubTaskIds();
                            subTaskIds.add(task.getId());
                            httpTaskManager.updateEpicStatus(epic);
                            httpTaskManager.updateEpicTime(epic);
                    }
                }
                if (lines[lines.length - 2].isBlank()) {
                    String[] historyIds = lines[lines.length - 1].split(",");
                    for (int j = 0; j < historyIds.length; j++) {
                        int id = Integer.parseInt(historyIds[j]);
                        if (httpTaskManager.getEpicById(id) != null) continue;
                        if (httpTaskManager.getTaskById(id) != null) continue;
                        if (httpTaskManager.getSubtaskById(id) != null) continue;
                    }
                }
            }
        } else {
            System.out.println("Ошибка. Файл пуст.");
        }
        return httpTaskManager;
    }

    public String getToken() {
        return token;
    }
}
