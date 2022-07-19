package com.exp.entity;

/**
 * 实验一饼图的分类数据类
 */
public class ClassData {
    private int value;       //分类数目
    private String name;     //分类名称

    public ClassData() {
    }

    public ClassData(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
