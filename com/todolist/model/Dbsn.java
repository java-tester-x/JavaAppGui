package com.todolist.model;

import java.util.*;
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
        DbsnDriver.INSTANCE.flushDBSN(dbh);
        DbsnDriver.INSTANCE.closeDBSN(dbh);
    }

    public void addTask(Task task) {
        DbsnDriver.INSTANCE.addFragm(dbh, task.toString());        
        // System.out.println("created task id: " + task.getId());
    }

    public void updateTask(Task task) {
        int n = findFragmentNumberByTaskId(task.getId());
        DbsnDriver.INSTANCE.setNom(dbh, n);
        DbsnDriver.INSTANCE.setFragm(dbh, task.toString());
        // System.out.println("nom = "+n+" for updated task id: " + task.getId());
    }

    public void removeTask(Task task) {
        int n = findFragmentNumberByTaskId(task.getId());
        DbsnDriver.INSTANCE.setNom(dbh, n);
        DbsnDriver.INSTANCE.cutFragm(dbh); // TODO: replace to delFragm method !!!

        // System.out.println("nom = "+n+" for removed task id: " + task.getId());
    }

    public int findFragmentNumberByTaskId(String id)
    {
        int rowCount = DbsnDriver.INSTANCE.countFragm(dbh);
        for(int i = 1; i <= rowCount; i++)
        {
            byte[] fragm = new byte[32567];
            DbsnDriver.INSTANCE.setNom(dbh, i);
            DbsnDriver.INSTANCE.getFragm(dbh, fragm, fragm.length);
            String s = new String(fragm).trim();

            String[] fields = s.replace("|", "::").split("::",-1);
            if (id.equals(fields[0])) {
                return i;
            }
        }

        return 0;
    }

    public Vector<Task> loadData()
    {
        int rowCount = DbsnDriver.INSTANCE.countFragm(dbh); // TODO replace to countActive() !!!
        Vector<Task> data = new Vector<Task>();
        for(int i = 1; i <= rowCount; i++)
        {
            byte[] fragm = new byte[32567];
            DbsnDriver.INSTANCE.setNom(dbh, i);
            DbsnDriver.INSTANCE.getFragm(dbh, fragm, fragm.length);
            String s = new String(fragm).trim();

            // StringBuilder builder = new StringBuilder();
            // builder.append(i);
            // builder.append("|");
            // builder.append(s);
            // Task aTask  = new Task(builder.toString());
            
            Task aTask  = new Task(s);
            
            data.add(aTask);
        }
        return data;
    }
    
}