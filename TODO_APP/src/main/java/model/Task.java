package model;

import java.time.LocalDate;

public class Task {
    private LocalDate datecreated;
    private String description;
    private String task;
    private String userid;


    public Task() {
    }

    public Task(LocalDate datecreated, String description, String task, String userid) {
        this.datecreated = datecreated;
        this.description = description;
        this.task = task;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public LocalDate getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(LocalDate datecreated) {
        this.datecreated = datecreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
