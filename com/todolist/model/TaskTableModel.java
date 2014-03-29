package com.todolist.model;

import java.util.Date;
import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;

public class TaskTableModel extends AbstractTableModel {

    public static final int ID_INDEX           = 0;
    public static final int TEXT_INDEX         = 1;
    public static final int CREATED_AT_INDEX   = 2;
    public static final int COMPLITED_TO_INDEX = 3;
    public static final int HIDDEN_INDEX       = 4;
    public static final int HIDDEN_INDEX2      = 5;

    /**
     * Represents a column of the table.
     */
    // static enum Column {
    //     ID("Id") {
    //         @Override public Object getValue(Task task) {
    //             return task.getId();
    //         }
    //         @Override public Class getColumnClass() {
    //             return Integer.class;
    //         }
    //         @Override public int getWidthInCharacters() {
    //             return 10;
    //         }
    //     },

    //     TEXT("Text") {
    //         @Override public Object getValue(Task task) {
    //             return task.getText();
    //             }
    //         @Override public Class getColumnClass() {
    //             return String.class;
    //         }
    //         @Override public int getWidthInCharacters() {
    //             return 20;
    //         }
    //     },

    //     CREATED_AT("Created At") {
    //         @Override public Object getValue(Task task) {
    //             return task.getCreationDate();
    //         }

    //         @Override public Class getColumnClass() {
    //             return Date.class;
    //         }

    //         @Override public int getWidthInCharacters() {
    //             return 25;
    //         }
    //     },

    //     COMPLITED_TO("Complited To") {
    //         @Override public Object getValue(Task task) {
    //             return task.getComplitionDate();
    //         }

    //         @Override public Class getColumnClass() {
    //             return Date.class;
    //         }

    //         @Override public int getWidthInCharacters() {
    //             return 25;
    //         }
    //     },

        
    //     private String displayName;

    //     private Column(String displayName) {
    //         assert displayName != null && displayName.length() > 0;
    //         this.displayName = displayName;
    //     }

    //     public String getDisplayName() {
    //         return displayName;
    //     }

    //     /**
    //      * Return the value for this column for the specified
    //      * person.
    //      */
    //     public abstract Object getValue(Person person);

    //     /**
    //      * Return the class of Object returned by this column.
    //      */
    //     public Class getColumnClass() {
    //       return String.class; // Default value
    //     }

    //     /**
    //      * Return the number of characters needed to display the
    //      * header and data for this column.
    //      */
    //     public abstract int getWidthInCharacters();
    // }


    protected String[]        columnNames = {"Id", "Text", "Created At", "Complited To", "", ""};    
    protected Vector<Task>    dataVector  = new Vector<Task>();
    protected Vector<Task>    bufferData;

    private Dbsn              db = new Dbsn("resources/TestDBSN");

    public TaskTableModel() {
        super();
        load();
    }

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

    public void refreshData() {
        load();
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
        if (column == HIDDEN_INDEX || column == ID_INDEX || column == HIDDEN_INDEX2)  {
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
            case HIDDEN_INDEX2:
                return JButton.class;
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
            case HIDDEN_INDEX2:
                JButton button = new JButton();
                return button;
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

    /**
     * [getRemovedTask description]
     * @return [description]
     */
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
     * [getCreatedTask description]
     * @return [description]
     */
    public Vector<Task> getCreatedTask() {
        Vector<Task> created = new Vector<Task>();
        for (Task t : dataVector) {
            if ( ! bufferData.contains(t)) {
                created.add(t);
            }
        }
        return created;
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
        Task task = new Task(0, 0, "");
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


    /**
     * [loadDataFromDbsn description]
     * @return [description]
     */
    private void load() {
        dataVector = db.loadData();
        bufferData = new Vector<Task>(dataVector);
    }

    /**
     * [saveDataToDbsn description]
     * @param changedTask [description]
     */
    public void save()
    {
        for (Task t : getChangedTask()) {
            db.updateTask(t);
            t.resetChangedFlag();
        }
        for (Task t : getRemovedTask()) {
            db.removeTask(t);
        }
        for (Task t : getCreatedTask()) {
            db.addTask(t);
            t.resetChangedFlag();
        }
        db.flushDbsn();
    }
}