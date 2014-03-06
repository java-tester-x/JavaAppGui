package com.todolist.controller;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.todolist.model.Task;

public class TaskController extends JPanel{

    private Task task;

    private JTextField taskTextField;

    public TaskController(Task controllerTask) {
        super();
        task = controllerTask;
        taskTextField = new JTextField(20);

        JButton saveButton= new JButton("Save");
        saveButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    task.setText(taskTextField.getText());
                }
            }
        );

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    taskTextField.setText("");
                    task.setText(taskTextField.getText());
                }
            }
        );

        setLayout(new FlowLayout());
        add(new JLabel("Text: "));
        add(taskTextField);
        add(saveButton);
        add(clearButton);
    }

}