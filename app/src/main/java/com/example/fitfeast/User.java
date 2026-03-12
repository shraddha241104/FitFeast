package com.example.fitfeast;

public class User {
    public String name;
    public int age;
    public float height;
    public float weight;
    public float bmi;
    public String category;

    public User() {} // Firebase needs empty constructor

    public User(String name, int age, float height, float weight, float bmi, String category) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.category = category;
    }
}

