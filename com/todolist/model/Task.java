package com.todolist.model;

import java.text.DateFormat;
// import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Date;
import java.util.UUID;


public class Task {

    private boolean hasChanged = false;

    private int     order;
    private String  id;
    private String  parentId;
    private String  text;
    private Date    creationDate;
    private Date    completionDate;
    private boolean isCompleted;

    public Task() {        
        this.id             = UUID.randomUUID().toString().replace("-", "");
        this.text           = "";
        this.creationDate   = Calendar.getInstance().getTime();
        this.completionDate = null;
        this.isCompleted    = false;
    }

    public Task(String s)
    {
        String[] fields = s.replace("|", "::").split("::",-1);
        try {
            DateFormat df       = DateFormat.getDateInstance();
            this.id             = fields[0];
            this.order          = Integer.parseInt(fields[1]);
            this.text           = fields[2];
            this.creationDate   = df.parse(fields[3]);
            this.completionDate = df.parse(fields[4]);
            this.isCompleted    = ! (fields[4]).isEmpty();
            this.parentId       = null;
        }
        catch (ParseException e) {}
        catch (Exception e) {}
    }

    public void setOrder(int order) {
        if (order == this.order) {
            return;            
        }
        this.order = order;
        this.hasChanged = true;
    }

    public void setId(String id) {
        if (id == null ? this.id == null : id.equals(this.id)) {
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
    }

    public void setParentId(String parentId) {
        if (parentId == null ? this.parentId == null : parentId.equals(this.parentId)) {
            return;            
        }
        this.parentId   = parentId;
        this.hasChanged = true;
    }

    public void setCreationDate(Date creationDate) {
        if (creationDate == null ? this.creationDate == null : creationDate.equals(this.creationDate)) {
            return;            
        }
        this.creationDate = creationDate;
        this.hasChanged   = true;
    }

    public void setCompletionDate(Date completionDate) {
        if (completionDate == null ? this.completionDate == null : completionDate.equals(this.completionDate)) {
            return;            
        }
        this.completionDate = completionDate;
        this.hasChanged     = true;
    }

    public void setCompletedFlag(boolean completed) {
        this.isCompleted = completed;
        if (completed) {
            setCompletionDate(Calendar.getInstance().getTime());
            return;
        }
        setCompletionDate(null);
    }

    public boolean getCompletedFlag() {
        return this.isCompleted;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
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
        list.add(id);
        list.add(Integer.toString(order));
        list.add(text);

        System.out.println(df.format(creationDate));

        list.add((creationDate   != null ? df.format(creationDate) : ""));
        list.add((completionDate != null ? df.format(completionDate) : ""));

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