package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Epic;
import com.yandexpraktikum.tasktracker.model.SubTask;
import com.yandexpraktikum.tasktracker.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message) {
            super(message);
        }
    }

    private final File file;
    private static final String HEADLINE = "id,type,name,status,description,epic";

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
                String line = "";
                for (Task task : getHistory()) {
                    line = line + task.getId() + ",";
                }
                line = line.substring(0, line.length()-1);
                fileWriter.write(line);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи файла.");
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
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        String content = "";
        try {
            content = Files.readString(Path.of(file.getPath()));
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла.");
        }
        if (!content.isEmpty()) {
            String[] lines = content.split("\n");
            int newId = 0;
            for (int i = 1; i < lines.length - 2; i++) {
                int id = Integer.parseInt(lines[i].substring(0, 1));
                if (id > newId) {
                    newId = id;
                }
            }
            fileBackedTasksManager.counter.setId(newId);
            for (int i = 1; i < lines.length - 2; i++) {
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
            }
            if (lines[lines.length - 1].length() > 0) {
                String[] historyIds = lines[lines.length - 1].split(",");
                for (int j = 0; j < historyIds.length; j++) {
                    int id = Integer.parseInt(historyIds[j]);
                    if (fileBackedTasksManager.getEpicById(id) != null) continue;
                    if (fileBackedTasksManager.getTaskById(id) != null) continue;
                    if (fileBackedTasksManager.getSubtaskById(id) != null) continue;
                }
            }
        } else {
            System.out.println("Ощибка. Файл пуст.");
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
        Task task1 = new Task("Уборка", "Убраться в комнате", "NEW");
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("Посуда", "Помыть посуду", "IN_PROGRESS");
        inMemoryTaskManager.addTask(task2);
        Epic epic1 = new Epic("Java", "Позаниматься Java");
        inMemoryTaskManager.addEpic(epic1);
        SubTask subTask1 = new SubTask("Теория", "Теория", "IN_PROGRESS", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("Практика", "Практика", "NEW", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask2);
        SubTask subTask3 = new SubTask("Зачет", "Зачет", "NEW", epic1.getId());
        inMemoryTaskManager.addSubTask(subTask3);
        Epic epic2 = new Epic("Ужин", "Покушать");
        inMemoryTaskManager.addEpic(epic2);
        SubTask subTask4 = new SubTask("Приготовить", "Приготовить", "IN_PROGRESS", epic2.getId());
        inMemoryTaskManager.addSubTask(subTask4);
        System.out.println(inMemoryTaskManager.getTasks());
        System.out.println(inMemoryTaskManager.getEpics());
        System.out.println(inMemoryTaskManager.getSubTasks());

        System.out.println("Запросы и вывод");
        System.out.println(inMemoryTaskManager.getTaskById(task1.getId()));
        System.out.println(inMemoryTaskManager.getTaskById(task2.getId()));
        System.out.println(inMemoryTaskManager.getEpicById(epic1.getId()));
        System.out.println(inMemoryTaskManager.getTaskById(task1.getId()));
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Удаление эпика и вывод");
        inMemoryTaskManager.removeEpicById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Чтение файла.");
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        FileBackedTasksManager.print(fileBackedTasksManager);
    }
}
