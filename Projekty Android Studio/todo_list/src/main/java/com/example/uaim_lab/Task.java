package com.example.uaim_lab;
// Task.java
import java.util.Date;

public class Task {
    private String title;
    private String description;
    private Date deadline;
    private String status;

    public Task(String title, String description, Date deadline, String status) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
