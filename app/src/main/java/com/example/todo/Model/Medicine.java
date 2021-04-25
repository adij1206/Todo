package com.example.todo.Model;

public class Medicine {
    private int id;
    private String name;
    private int hr;
    private int min;
    private int set;
    private String dateAdded;

    public Medicine(){}

    public Medicine(int id, String name, int hr, int min, int set,String dateAdded) {
        this.id = id;
        this.name = name;
        this.hr = hr;
        this.min = min;
        this.set = set;
        this.dateAdded =dateAdded;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }
}
