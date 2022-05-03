import java.util.HashMap;

public class Manager {

    private HashMap<Integer,Task> taskList;
    private HashMap<Integer,Epic> epicList;
    private HashMap<Integer,SubTask> subTaskList;

    public Manager() {
        this.taskList = new HashMap<>();
        this.epicList = new HashMap<>();
        this.subTaskList = new HashMap<>();
    }

    public void addTask (Task task) { //добавить задачу
        taskList.put(task.getId(), task);
    }

    public void addEpic (Epic epic) { //добавить эпик
        epicList.put(epic.getId(), epic);
    }

    public void addSubTask (SubTask subTask) { //добавить подзадачу
        subTaskList.put(subTask.getId(), subTask);
        Epic newEpic = epicList.get(subTask.getEpicId());
        newEpic.subTaskIds.add(subTask.getId());
        epicList.put(newEpic.getId(), newEpic);
    }

    public void printTaskList() { //вывод списка задач
        System.out.println("Список задач:");
        for (Task task : taskList.values()) {
            System.out.println(task);
        }
    }

    public void printEpicList() { //вывод списка эпиков
        System.out.println("Список эпиков:");
        for (Epic epic: epicList.values()) {
            System.out.println(epic);
        }
    }

    public void printSubTaskList() { //вывод списка подзадач
        System.out.println("Список подзадач:");
        for (SubTask subTask: subTaskList.values()) {
            System.out.println(subTask);
        }
    }

    public void clearAllTasks() { //удалить все задачи
        taskList.clear();
        System.out.println("Все задачи удалены.");
    }

    public void clearAllEpics() { //удалить все эпики
        epicList.clear();
        System.out.println("Все эпики удалены.");
    }

    public void clearAllSubtasks() { //удалить все подзадачи
        subTaskList.clear();
        System.out.println("Все подзадачи удалены.");
    }

    public Task getTaskById(int id) {
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else {
            System.out.println("Такой задачи нет.");
            return null;
        }
    }

    public Epic getEpicById(int id) {
        if (epicList.containsKey(id)) {
            return epicList.get(id);
        } else {
            System.out.println("Такого эпика нет.");
            return null;
        }
    }

    public SubTask getSubtaskById(int id) {
        if (subTaskList.containsKey(id)) {
            return subTaskList.get(id);
        } else {
            System.out.println("Такой подзадачи нет.");
            return null;
        }
    }

    public void removeTaskById(int id) {  //удаление задачи по ИД
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else {
            System.out.println("Такой задачи нет.");
        }
    }

    public void removeEpicById(int id) {
        if (epicList.containsKey(id)) {
            Epic newEpic = epicList.get(id);
            for (Integer newId: newEpic.subTaskIds) {
                subTaskList.remove(newId);
            }
            epicList.remove(id);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    public void removeSubtaskById(int id) {
        if (subTaskList.containsKey(id)) {
            subTaskList.remove(id);
        } else {
            System.out.println("Такой подзадачи нет.");
        }
    }

    public void getSubtaskListFromEpic(int id) { //вывод списка подзадач эпика
        System.out.println("Список подзадач:");
        Epic newEpic = epicList.get(id);
        for (Integer newId: newEpic.subTaskIds) {
            SubTask newSubTask = subTaskList.get(newId);
            System.out.println(newSubTask.name);
        }
    }

    public void updateTask(Task task, String status) {
        task.setStatus(status);
        taskList.put(task.getId(), task);
    }

    public void updateSubTask(SubTask subTask, String status) {
        subTask.setStatus(status);
        subTaskList.put(subTask.getId(), subTask);
        Epic newEpic = epicList.get(subTask.getEpicId());
        boolean isInProgress = false;
        boolean isDone = true;
        for (Integer newId: newEpic.subTaskIds) {
            SubTask newSubTask = subTaskList.get(newId);
            if (!newSubTask.getStatus().equals("NEW")) {
                isInProgress = true;
            }
        }
        if (isInProgress) {
            newEpic.setStatus("IN_PROGRESS");
        }
        for (Integer newId: newEpic.subTaskIds) {
            SubTask newSubTask = subTaskList.get(newId);
            if (!newSubTask.getStatus().equals("DONE")) {
                isDone = false;
            }
        }
        if (isDone) {
            newEpic.setStatus("DONE");
        }
        epicList.put(newEpic.getId(), newEpic);
    }

    public void updateEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
    }
}
