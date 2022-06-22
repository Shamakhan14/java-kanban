package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Counter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static class ManagerSaveException extends Exception {
        public ManagerSaveException(String message) {
            super(message);
        }
    }

    public FileBackedTasksManager() {
        super();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic (Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask (SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subTask = super.getSubtaskById(id);
        save();
        return subTask;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    protected void updateEpicStatus(Epic epic) {
        super.updateEpicStatus(epic);
        save();
    }

    public void save() {
        try {
            try (Writer fileWriter = new FileWriter("save.txt")) {
                fileWriter.write("id,type,name,status,description,epic\n");
                for (Task task: tasks.values()) {
                    fileWriter.write(task.toString() + "\n");
                }
                for (Epic epic: epics.values()) {
                    fileWriter.write(epic.toString() + "\n");
                }
                for (SubTask subTask: subTasks.values()) {
                    fileWriter.write(subTask.toString() + "\n");
                }
                fileWriter.write("\n");
                for (Task task: getHistory()) {
                    fileWriter.write(task.getId() + ",");
                }
                fileWriter.write("\n" + Counter.getCurrentId() + "\n\n");
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка при записи файла.");
            }
        } catch (ManagerSaveException saveException) {
            System.out.println(saveException.getMessage());
        }
    }


    public static Task fromString(String value) {
        String[] values = value.split(","); //append=true для проверки
        if (values[1].equals("TASK")) {
            Task task = new Task(values[2], values[4], values[3]);
            task.setId(Integer.parseInt(values[0]));
            return task;
        } else if (values[1].equals("EPIC")) {
            Epic epic = new Epic(values[2], values[4]);
            epic.setId(Integer.parseInt(values[0]));
            return epic;
        } else if (values[1].equals("SUBTASK")) {
            SubTask subTask = new SubTask(values[2], values[4], values[3], Integer.parseInt(values[5]));
            subTask.setId(Integer.parseInt(values[0]));
            return subTask;
        }
        return null;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        String content = new String();
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла.");
        }
        String[] lines = content.split("\n");
        Counter.setId(Integer.parseInt(lines[lines.length-1]));
        String[] historyIds = lines[lines.length-2].split(",");
        for (int i = 1; i < lines.length-3; i++) {
            Task task = fromString(lines[i]);
            String[] line = lines[i].split(",");
            if (line[1].equals("TASK")) {
                fileBackedTasksManager.tasks.put(task.getId(), task);
            } else if (line[1].equals("EPIC")) {
                fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
            } else if (line[1].equals("SUBTASK")) {
                fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                Epic epic = fileBackedTasksManager.epics.get(((SubTask) task).getEpicId());
                List<Integer> subTaskIds = epic.getSubTaskIds();
                subTaskIds.add(task.getId());
                fileBackedTasksManager.updateEpicStatus(epic);
            }
            for (int j = 0; j < historyIds.length; j++) {
                if (Integer.parseInt(historyIds[j]) == task.getId()) {
                    fileBackedTasksManager.inMemoryHistoryManager.addTaskToHistory(task);
                }
            }
        }
        return fileBackedTasksManager;
    }

    public static void print(FileBackedTasksManager taskManager) {
        System.out.println("Таски:");
        for (Task task: taskManager.tasks.values()) {
            System.out.println(task.toString());
        }
        System.out.println("Эпики:");
        for (Epic epic: taskManager.epics.values()) {
            System.out.println(epic.toString());
        }
        System.out.println("Сабтаски:");
        for (SubTask subTask: taskManager.subTasks.values()) {
            System.out.println(subTask.toString());
        }
        System.out.println("История:");
        List<Task> history = taskManager.getHistory();
        for (Task task: history) {
            System.out.print(task.getId() + ",");
        }
        System.out.println("");
    }
}
