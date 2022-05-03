public class Main {

    public static void main(String[] args) {

        Counter counter = new Counter();
        Manager manager = new Manager();

        Epic epic1 = new Epic("Помыть посуду", "Взять губку и помыть посуду", counter.getNewId());
        SubTask subTask1 = new SubTask("Взять губку",
                "Найти губку и взять в руки", counter.getNewId(), epic1.getId());
        SubTask subTask2 = new SubTask("Взять моющее средство",
                "Найти средство и достать его", counter.getNewId(), epic1.getId());
        manager.addEpic(epic1);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        Epic epic2 = new Epic("Пропылесосить квартиру",
                "Откопать пылесос и пропылесосить", counter.getNewId());
        SubTask subTask3 = new SubTask("Распутить шнур",
                "Распутать шнур пылесоса", counter.getNewId(), epic2.getId());
        manager.addEpic(epic2);
        manager.addSubTask(subTask3);

        manager.printTaskList();
        manager.printEpicList();
        manager.printSubTaskList();

        subTask1.name = "Откопать губку";
        manager.updateSubTask(subTask1, "IN_PROGRESS");
        manager.updateSubTask(subTask3, "DONE");
        manager.printTaskList();
        manager.printEpicList();
        manager.printSubTaskList();

        manager.removeEpicById(epic2.getId());
        manager.printTaskList();
        manager.printEpicList();
        manager.printSubTaskList();
    }
}
