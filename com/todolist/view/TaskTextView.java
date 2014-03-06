package com.todolist.view;

import java.util.*;
import javax.swing.*;

import com.todolist.model.Task;

public class TaskTextView extends AbstractTaskView {

    private JTextField taskTextField = new JTextField(20);

    public TaskTextView(Task task) {
        super(task);
        taskTextField.setEditable(false);
        add(new JLabel("Task: "));
        add(taskTextField);

        updateDisplay();
    }

    public void updateDisplay() {
        taskTextField.setText(getTask().getText());
    }
}