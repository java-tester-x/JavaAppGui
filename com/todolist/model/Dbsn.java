package com.todolist.model;

import java.util.Vector;

import com.todolist.dao.DbsnDriver;

public class Dbsn {

    private int dbh;

    public Dbsn(String dbsnPath) {
        this.connect(dbsnPath);
    }


    public void connect(String dbsnPath) {
        dbh = DbsnDriver.INSTANCE.openDBSN(dbsnPath);
    }

    public void flush() {
        DbsnDriver.INSTANCE.flushDBSN(dbh);
    }

    public void disconnect() {
        flush();
        DbsnDriver.INSTANCE.closeDBSN(dbh);
    }

    public void addTask(Task task) {
        DbsnDriver.INSTANCE.addFragm(dbh, task.toString());
    }

    public void updateTask(Task task) {
        DbsnDriver.INSTANCE.setNom(dbh, task.getId());
        DbsnDriver.INSTANCE.setFragm(dbh, task.toString());
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
            Task aTask  = new Task(text);
            //Task aTask  = new Task(i, 0, text);
            
            data.add(aTask);
        }
        return data;
    }
    
}