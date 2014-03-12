package com.todolist.model;

import java.util.Date;
import java.util.Observable;

public class Task extends Observable {

    private int     id;
    private int     parentId;
    private String  text;
    private Date    creationDate;
    private Date    completionDate;

    public Task(int id, int parentID, String text) {
        this.id           = id;
        this.parentId     = parentId;
        this.creationDate = new Date();
        setText(text);
    }

    public Task() {
        this(0, 0, null);
    }

    public void setText(String text) {
        this.text = text;
        setChanged();
        notifyObservers();
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
        setChanged();
        notifyObservers();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
        setChanged();
        notifyObservers();
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
        setChanged();
        notifyObservers();
    }

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public String getText() {
        return text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

}