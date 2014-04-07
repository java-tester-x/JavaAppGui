package com.todolist.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Date;


// public class Task extends Observable {
public class Task {

    private boolean hasChanged = false;

    private int     id = 1;
    private int     order;
    private int     parentId;
    private String  text;
    private Date    creationDate;
    private Date    completionDate;

    public Task() {
        this("");
    }

    public Task(String s)
    {
        String[] fields = fields = s.replace("|", "::").split("::",-1);

        System.out.println("Task:" + s);

        try {
            DateFormat df       = DateFormat.getDateInstance();
            this.id             = Integer.parseInt(fields[0]);
            this.order          = Integer.parseInt(fields[1]);
            this.text           = fields[2];
            this.creationDate   = df.parse(fields[3]);
            this.completionDate = df.parse(fields[4]);
            this.parentId       = 0;
        }
        catch (ParseException e) {}
        catch (Exception e) {}
    }

    public void setId(int id) {
        if (id == this.id) {
            return;            
        }
        this.id = id;
        this.hasChanged = true;
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

    public void setOrder(int order) {
        if (order == this.order) {
            return;            
        }
        this.order = order;
        this.hasChanged = true;
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

    public int getOrder() {
        return order;
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

    public void resetChangedFlag() {
        hasChanged = false;    
    }

    public String toString()
    {
        List<String> list = new ArrayList<String>();
        DateFormat   df   = DateFormat.getDateInstance();
        list.add(Integer.toString(order));
        list.add(text);
        list.add((creationDate != null ? df.format(creationDate) : ""));
        list.add((creationDate != null ? df.format(completionDate) : ""));

        // Remove all empty values
        // list.removeAll(Arrays.asList("", null));

        // If this list is empty, it only contained blank values
        if( list.isEmpty()) {
            return "";
        }

        // Format the ArrayList as a string, similar to implode
        StringBuilder builder = new StringBuilder();
        builder.append(list.remove(0));
        for( String s : list) {
            builder.append("|");
            builder.append(s);
        }

        return builder.toString();
    }

}