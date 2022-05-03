import java.util.ArrayList;

public class Epic extends Task{

    public ArrayList<Integer> subTaskIds;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        status = "NEW";
        subTaskIds = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}
