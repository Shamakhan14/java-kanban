package com.yandexpraktikum.tasktracker.service;

import com.yandexpraktikum.tasktracker.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> historyTable;

    class CustomLinkedList {

        private Node head;
        private Node tail;
        private int size = 0;

        public void linkLast(Task element) {
            Node oldTail = tail;
            Node newNode = new Node(tail, element, null);
            tail = newNode;
            if (oldTail != null) {
                oldTail.next = newNode;
            } else {
                head = newNode;
            }
            size++;
        }

        public ArrayList<Task> getTasks(){
            ArrayList<Task> result = new ArrayList<>(size);
            for(Node temp = head;temp!=null;temp = temp.next){
                result.add(temp.data);
            }
            return result;
        }

        public void removeNode(Node x) {
            Node prev = x.prev;
            Node next = x.next;
            if(prev == null){
                this.head = next;
            }else{
                prev.next = next;
                x.prev = null;
            }
            if(next == null){
                this.tail = prev;
            }else{
                next.prev = prev;
                x.next = null;
            }
            x.data = null;
            this.size--;
        }

        public Node getLastNode() {
            return tail;
        }
    }

    private CustomLinkedList history;

    public InMemoryHistoryManager() {
        this.history = new CustomLinkedList();
        this.historyTable = new HashMap<>();
    }

    @Override
    public void addTaskToHistory(Task task) {
        remove(task.getId());
        history.linkLast(task);
        historyTable.put(task.getId(), history.getLastNode());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history.getTasks());
    }

    @Override
    public void remove (int id) {
        if (historyTable.containsKey(id)) {
            history.removeNode(historyTable.get(id));
            historyTable.remove(id);
        }
    }
}
