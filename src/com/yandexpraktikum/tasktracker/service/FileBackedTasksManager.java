package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;
import com.yandexpraktikum.tasktracker.util.Status;
import com.yandexpraktikum.tasktracker.util.TaskType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message) {
            super(message);
        }
    }

    private final File file;
    private static final String HEADLINE = "id,type,name,status,description,startTime,duration,epicId";

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
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

    @Override
    protected void updateEpicTime(Epic epic) {
        super.updateEpicTime(epic);
        save();
    }

    public void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write(HEADLINE + "\n");
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
            if (!getHistory().isEmpty()) {
                StringBuilder line = new StringBuilder();
                for (Task task: getHistory()) {
                    line.append(task.getId() + ",");
                }
                line.deleteCharAt(line.length()-1);
                fileWriter.write(line.toString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла.");
        }
    }

    private static Task fromString(String value) {
        String[] values = value.split(","); //", append = true" для проверки
        switch (TaskType.valueOf(values[1])) {
            case TASK:
                Task task = new Task(values[2], values[4], values[3], Integer.parseInt(values[6]),
                        LocalDateTime.parse(values[5]));
                task.setId(Integer.parseInt(values[0]));
                return task;
            case EPIC:
                Epic epic = new Epic(values[2], values[4], values[3], Integer.parseInt(values[6]),
                        LocalDateTime.parse(values[5]));
                epic.setId(Integer.parseInt(values[0]));
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(values[2], values[4], values[3], Integer.parseInt(values[6]),
                        LocalDateTime.parse(values[5]), Integer.parseInt(values[7]));
                subTask.setId(Integer.parseInt(values[0]));
                return subTask;
            default:
                return null;
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String content = "";
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла.");
        }
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
                fileBackedTasksManager.counter.setId(newId);
                for (int i = 1; i < lineNum; i++) {
                    Task task = fromString(lines[i]);
                    String[] line = lines[i].split(",");
                    switch (TaskType.valueOf(line[1])) {
                        case TASK:
                            fileBackedTasksManager.tasks.put(task.getId(), task);
                            fileBackedTasksManager.sortedSet.add(task);
                            break;
                        case EPIC:
                            fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            fileBackedTasksManager.subTasks.put(task.getId(), (SubTask) task);
                            fileBackedTasksManager.sortedSet.add(task);
                            Epic epic = fileBackedTasksManager.epics.get(((SubTask) task).getEpicId());
                            List<Integer> subTaskIds = epic.getSubTaskIds();
                            subTaskIds.add(task.getId());
                            fileBackedTasksManager.updateEpicStatus(epic);
                            fileBackedTasksManager.updateEpicTime(epic);
                    }
                }
                if (lines[lines.length - 2].isBlank()) {
                    String[] historyIds = lines[lines.length - 1].split(",");
                    for (int j = 0; j < historyIds.length; j++) {
                        int id = Integer.parseInt(historyIds[j]);
                        if (fileBackedTasksManager.getEpicById(id) != null) continue;
                        if (fileBackedTasksManager.getTaskById(id) != null) continue;
                        if (fileBackedTasksManager.getSubtaskById(id) != null) continue;
                    }
                }
            }
        } else {
            System.out.println("Ошибка. Файл пуст.");
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

    public static void main(String[] args) {

        File file = new File("save.txt");
        TaskManager inMemoryTaskManager = new FileBackedTasksManager(file);

        //Заполнение и вывод
        Task task1 = new Task("name", "description", "NEW", 15, LocalDateTime.now());
        inMemoryTaskManager.addTask(task1);
        Epic epic1 = new Epic("name2", "desc2");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("name3", "desc3", "NEW", 20, LocalDateTime.now().plusMinutes(30), epic1.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        Task task4 = inMemoryTaskManager.getTaskById(task1.getId());
        Task task5 = inMemoryTaskManager.getTaskById(task1.getId());

        FileBackedTasksManager taskManager = FileBackedTasksManager.loadFromFile(file);
        FileBackedTasksManager.print(taskManager);
    }
}
