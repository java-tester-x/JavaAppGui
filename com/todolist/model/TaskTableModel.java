package com.todolist.model;

import java.util.Date;
import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TaskTableModel extends AbstractTableModel {

    public static final int ID_INDEX           = 0;
    public static final int TEXT_INDEX         = 1;
    public static final int CREATED_AT_INDEX   = 2;
    public static final int COMPLITED_TO_INDEX = 3;
    public static final int HIDDEN_INDEX       = 4;

    protected String[]        columnNames = {"Id", "Text", "Created At", "Complited To", ""};    
    protected Vector<Task>    dataVector  = new Vector<Task>();
    protected Vector<Task>    bufferData;


    public TaskTableModel(Vector<Task> dataVector) {
        super();
        this.dataVector = dataVector;
        this.bufferData = new Vector<Task>(dataVector);
    }

    public void setData(Vector<Task> dataVector) {  
        this.dataVector = dataVector;  
        this.bufferData = new Vector<Task>(dataVector);
        fireTableDataChanged();  
    }  

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {
        return dataVector.size();
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == HIDDEN_INDEX || column == ID_INDEX)  {
            return false;
        }
        return true;
    }

    @Override
    public Class getColumnClass(int column)
    {
        switch (column) {
            case CREATED_AT_INDEX:
            case COMPLITED_TO_INDEX:
                return Date.class;            
            case ID_INDEX:
                return Integer.class;
            case TEXT_INDEX:
                return String.class;
            default: 
                return Object.class;
        }
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        Task task = (Task) dataVector.get(row);
        switch (column) {
            case ID_INDEX:
                return task.getId();
            case TEXT_INDEX:
                return task.getText();
            case CREATED_AT_INDEX:
                return task.getCreationDate();
            case COMPLITED_TO_INDEX:
                return task.getCompletionDate();
            default:
                return new Object();
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        Task task = (Task) dataVector.get(row);
        switch (column) {
            case ID_INDEX:
                // int id = (Integer) value;
                task.setId((Integer) value);
                break;
            case TEXT_INDEX:
                task.setText((String) value);
                break;
            case CREATED_AT_INDEX:
                task.setCreationDate((Date) value);
                break;
            case COMPLITED_TO_INDEX:
                task.setCompletionDate((Date) value);
                break;
            default:
                System.out.println("invalid index");
        }
        fireTableCellUpdated(row, column);
    }

    /**
     * [addTask description]
     * @param task [description]
     */
    public void addTask(Task task) {
        dataVector.add(task);
        fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }

    /**
     * [getTaskAt description]
     * @param  row [description]
     * @return     [description]
     */
    public Task getTaskAt(int row) {
        return dataVector.get(row);
    }

    /**
     * [getChangedPeople description]
     * @return [description]
     */
    public Vector<Task> getChangedTask() {
        Vector<Task> changed = new Vector<Task>();
        for (Task t : dataVector) {
            if (t.hasChanged()) {
                changed.add(t);
            }
        }
        return changed;    
    }

    public Vector<Task> getRemovedTask() {
        Vector<Task> removed = new Vector<Task>();
        for (Task t : bufferData) {
            if ( ! dataVector.contains(t)) {
                removed.add(t);
            }
        }
        return removed;
    }

    /**
     * [hasEmptyRow description]
     * @return [description]
     */
    public boolean hasEmptyRow()
    {
        if (dataVector.size() == 0) {
            return false;
        }

        Task task = (Task) dataVector.get(dataVector.size() - 1);
        if  (   task.getId() == 0
            // &&  task.getText().trim().equals("") 
            // &&  task.getCreationDate().toString().equals("")
            // &&  task.getCompletionDate().toString().equals("")
            )
        {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * [addEmptyRow description]
     */
    public void addEmptyRow() {
        Task task = new Task(dataVector.size(), 0, "");
        dataVector.add(task);
        fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }

    /**
     * [removeRow description]
     * @param row [description]
     */
    public void removeRow(int row)
    {
        dataVector.remove(row);
        fireTableRowsDeleted(row, row);
    }

}