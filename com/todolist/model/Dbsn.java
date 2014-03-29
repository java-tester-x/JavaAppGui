package com.todolist.model;

import java.util.Vector;
import com.todolist.dao.DbsnDriver;

public class Dbsn {

    private int dbh;

    public Dbsn(String dbsnPath) {
        this.connectToDbsn(dbsnPath);
    }


    public void connectToDbsn(String dbsnPath) {
        dbh = DbsnDriver.INSTANCE.openDBSN(dbsnPath);
    }

    public void flushDbsn() {
        DbsnDriver.INSTANCE.flushDBSN(dbh);
    }

    public void disconnectFromDbsn() {
        flushDbsn();
        DbsnDriver.INSTANCE.closeDBSN(dbh);
    }

    public void addTask(Task task) {
        DbsnDriver.INSTANCE.addFragm(dbh, task.getText());
    }

    public void updateTask(Task task) {
        DbsnDriver.INSTANCE.setNom(dbh, task.getId());
        DbsnDriver.INSTANCE.setFragm(dbh, task.getText());
    }

    public void removeTask(Task task) {
        DbsnDriver.INSTANCE.setNom(dbh, task.getId());
        DbsnDriver.INSTANCE.cutFragm(dbh);
    }

    public Vector<Task> loadData()
    {
        int rowCount = DbsnDriver.INSTANCE.countFragm(dbh);
        Vector<Task> data = new Vector<Task>();
        for(int i = 1; i <= rowCount; i++)
        {
            byte[] fragm = new byte[32567];
            DbsnDriver.INSTANCE.setNom(dbh, i);
            DbsnDriver.INSTANCE.getFragm(dbh, fragm, fragm.length);
            String text = new String(fragm).trim();
            Task aTask  = new Task(i, 0, text);
            data.add(aTask);
        }
        return data;
    } 
}