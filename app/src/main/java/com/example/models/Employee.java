package com.example.models;

import java.io.Serializable;

public class Employee implements Serializable {
    private String id;
    private String name;
    private String phone;
    private String birthPlace;

    public String getBirthplace() {
        return birthPlace;
    }

    public void setBirthplace(String birthplace) {
        this.birthPlace = birthplace;
    }

    public Employee() {
    }
    public Employee(String id, String name, String phone, String birthplace) {
        this(id,name,phone);
        this.birthPlace = birthplace;
    }

    public Employee(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
