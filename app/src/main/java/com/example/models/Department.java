package com.example.models;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Department {
    private String id;
    private String name;

    private ArrayList<Employee> listOfEmployee;

    public Department() {
        listOfEmployee= new ArrayList<>();
    }

    public Department(String id, String name) {
        this.id = id;
        this.name = name;
        listOfEmployee= new ArrayList<>();
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

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
    public void addEmployee(Employee emp) {
        listOfEmployee.add(emp);
    }
    public void addListEmployee(ArrayList<Employee> list) {
        listOfEmployee.addAll(list);
    }
    public ArrayList<Employee> getListOfEmployee() {
        return listOfEmployee;
    }
//    public void setListOfEmployee(ArrayList<Employee> listOfEmployee) {
//        this.listOfEmployee = listOfEmployee;
//    }
    public void removeEmployee(Employee emp) {
        listOfEmployee.remove(emp);
    }
    public void removeListEmployee(ArrayList<Employee> list) {
        listOfEmployee.removeAll(list);
    }
}
