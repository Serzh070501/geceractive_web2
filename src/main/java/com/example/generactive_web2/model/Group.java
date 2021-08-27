package com.example.generactive_web2.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private int id;
    private String name;
    private Group parentGroup;
    private List<Group> subGroups = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public void addSubGroup(Group group) {
        this.subGroups.add(group);
        group.setParentGroup(this);
    }

    public void addItem(Item item) {
        this.items.add(item);
        item.setGroup(this);
    }

    public void addItems(List<Item> items) {
        for (Item item : items) {
            addItem(item);
        }
    }

    public void print(int level) {
        System.out.printf("GROUP - id: {%d} {%s}%n", id, name);
        printSubGroups(++level);
        printItems(level);
    }

    private void printSubGroups(int level) {
        String subLevelPrefix = " ".repeat(level) ;
        for (Group group : subGroups) {
            System.out.print(subLevelPrefix);
            group.print(level);
        }
    }

    private void printItems(int level) {
        String subLevelPrefix = "  ".repeat(level);
        for (Item item : items) {
            System.out.print(subLevelPrefix);
            item.print();
        }
    }
}
