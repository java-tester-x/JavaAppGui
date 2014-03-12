package com.todolist.model;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;

class TaskTableModel extends AbstractTableModel {

    public static final int ID_INDEX           = 0;
    public static final int TEXT_INDEX         = 1;
    public static final int CREATED_AT_INDEX   = 2;
    public static final int COMPLITED_TO_INDEX = 3;

    protected String[]  columnNames = {"Id", "Text", "Created At", "Complited To"};    
    protected Vector    dataVector  = new Vector();

    public TaskTableModel(Vector dataVector) {
        super();
        this.dataVector = dataVector;
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
        if (column == TEXT_INDEX) {
            return true;
        }
        return false;
    }

    @Override
    public Class getColumnClass(int column)
    {
        switch (column) {
            case CREATED_AT_INDEX:
            case COMPLITED_TO_INDEX:
                return Date.class;            
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

    public boolean hasEmptyRow()
    {
        if (dataVector.size() == 0) {
            return false;
        }

        Task task = (Task) dataVector.get(dataVector.size() - 1);
        if  (   task.getId() == 0
            &&  task.getText().trim().equals("") 
            &&  task.getCreationDate().toString().equals("")
            &&  task.getCompletionDate().toString().equals("")
            )
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void addEmptyRow() {
        dataVector.add(new Task());
        fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }
}