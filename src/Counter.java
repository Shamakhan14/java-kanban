public class Counter {

    private int id;


    public Counter() {
        id = 0;
    }

    public int getNewId() {
        id++;
        return id;
    }
}
