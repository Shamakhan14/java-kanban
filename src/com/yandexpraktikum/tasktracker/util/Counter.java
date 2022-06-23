package com.yandexpraktikum.tasktracker.util;

public class Counter {

    private int id;

    public Counter() {
        this.id = 0;
    }

    public int getNewId() {
        id++;
        return id;
    }

    public int getCurrentId() {
        return id;
    }

    public void setId(int newId) {
        id = newId;
    }
}
