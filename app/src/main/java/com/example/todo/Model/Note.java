package com.example.todo.Model;

public class Note {
    private String title;
    private String description;
    private String dateAdded;
    private int id;

    public Note() {
    }

    public Note(String title, String description, String dateAdded, int id) {
        this.title = title;
        this.description = description;
        this.dateAdded = dateAdded;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
