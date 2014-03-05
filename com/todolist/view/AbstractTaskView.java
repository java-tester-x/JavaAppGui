
package com.todolist.view;

import java.util.*;
import java.awt.*;
import javax.swing.JPanel;

import com.todolist.model.Task;

abstract class AbstractTaskView 
extends        JPanel
implements     Observer {

    private Task task;

    public AbstractTaskView(Task observableTask) 
    throws NullPointerException 
    {
        if (observableTask == null) {
            throw new NullPointerException();
        }

        task = observableTask;

        task.addObserver(this);
    }

    public Task getTask() {
        return task;
    }

    protected abstract void updateDisplay();

    public void update(Observable observable, Object object) {
        updateDisplay();
    }

}