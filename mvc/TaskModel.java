package mvc;

import java.util.Date;
import java.util.Observable;

public class TaskModel extends Observable {

    private int     id;
    private int     parentId;
    private String  text;
    private Date    creationDate;
    private Date    completionDate;

    public TaskModel(int id, int parentID, String text) {
        // 
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getId() {
        return this.id;
    }

    public int getParentId() {
        return this.parentId;
    }

    public String getText() {
        return this.text;
    }
}