package com.todolist.model;

import java.util.Date;
// import java.util.Observable;

// public class Task extends Observable {
public class Task {

    private boolean hasChanged = false;

    private int     id;
    private int     parentId;
    private String  text;
    private Date    creationDate;
    private Date    completionDate;

    public Task(int id, int parentID, String text) {
        this.id             = id;
        this.parentId       = parentId;
        this.creationDate   = null;
        this.completionDate = null;
        this.text           = text;
    }

    public Task() {
        this(0, 0, null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        if (text == null ? this.text == null : text.equals(this.text)) {
            return;            
        }
        this.text       = text;
        this.hasChanged = true;
        // setChanged();
        // notifyObservers();
    }

    public void setParentId(int parentId) {
        if (parentId == 0 ? this.parentId == 0 : this.parentId == parentId) {
            return;            
        }
        this.parentId   = parentId;
        this.hasChanged = true;
        // setChanged();
        // notifyObservers();
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null ? this.creationDate == null : creationDate.equals(this.creationDate)) {
            return;            
        }
        this.creationDate = creationDate;
        this.hasChanged   = true;
        // setChanged();
        // notifyObservers();
    }

    public void setCompletionDate(Date completionDate) {
        if (completionDate == null ? this.completionDate == null : completionDate.equals(this.completionDate)) {
            return;            
        }
        this.completionDate = completionDate;
        this.hasChanged     = true;
        // setChanged();
        // notifyObservers();
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

    public boolean hasChanged() {
        return hasChanged;
    }
}