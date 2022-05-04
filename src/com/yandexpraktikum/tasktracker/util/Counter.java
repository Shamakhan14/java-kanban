package com.yandexpraktikum.tasktracker.util;

public class Counter {

    private static int id = 0;

    public static int getNewId() {
        id++;
        return id;
    }
}
