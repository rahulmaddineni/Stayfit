package com.example.maddi.fitness;

public class User {
    public String name;
    public String phone;
    public String gender;
    public int age;
    public String height;
    public float weight;
    public int stepgoal;
    public int caloriegoal;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String phone, String gender, int age, String height, float weight, int stepgoal, int caloriegoal) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.stepgoal = stepgoal;
        this.caloriegoal = caloriegoal;
    }
}
