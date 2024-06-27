package com.example.lab2;

// Obj.java
public class Obj {
    private String id;
    private String name;
    private int age;

    public Obj() {
        // Constructor rỗng cần thiết cho Firestore
    }

    public Obj(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

