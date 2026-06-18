package com.example.models;

import java.io.Serializable;

public class Category implements Serializable {
    private String cateId;
    private String cateName;
    private String cateDesc;

    public Category() {
    }

    public Category(String cateId, String cateName, String cateDesc) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.cateDesc = cateDesc;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateDesc() {
        return cateDesc;
    }

    public void setCateDesc(String cateDesc) {
        this.cateDesc = cateDesc;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cateId='" + cateId + '\'' +
                ", cateName='" + cateName + '\'' +
                ", cateDesc='" + cateDesc + '\'' +
                '}';
    }
}
