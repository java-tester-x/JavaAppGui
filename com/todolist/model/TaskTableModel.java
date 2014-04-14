package com.todolist.model;

import java.util.Date;
import java.util.Collection;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;

public class TaskTableModel extends AbstractTableModel {

    public static final int ID_INDEX           = 0;
    public static final int ORDER_INDEX        = 1;
    public static final int TEXT_INDEX         = 2;
    public static final int CREATED_AT_INDEX   = 3;
    public static final int COMPLITED_TO_INDEX = 4;
    public static final int HIDDEN_INDEX2      = 5;

    /**
     * Represents a column of the table.
     */
    public static enum Column {

        ID("Id") {
            @Override public Object getValue(Task task) {
                return task.getId();
            }
            @Override public void setValue(Task task, Object value) {
                task.setId((String) value);
            }
            @Override public Class getColumnClass() {
                return String.class;
            }
            @Override public int getWidthInCharacters() {
                return 30;
            }
            @Override public int getColumnIndex() {
                return 0;
            }
        },

        ORDER("Order") {
            @Override public Object getValue(Task task) {
                return task.getOrder();
            }
            @Override public void setValue(Task task, Object value) {
                task.setOrder((Integer) value);
            }
            @Override public Class getColumnClass() {
                return Integer.class;
            }
            @Override public int getWidthInCharacters() {
                return 10;
            }
            @Override public int getColumnIndex() {
                return 1;
            }
        },

        TEXT("Text") {
            @Override public Object getValue(Task task) {
                return task.getText();
            }
            @Override public void setValue(Task task, Object value) {
                task.setText((String) value);
            }
            @Override public Class getColumnClass() {
                return String.class;
            }
            @Override public int getWidthInCharacters() {
                return 50;
            }
            @Override public int getColumnIndex() {
                return 2;
            }
        },

        CREATED_AT("Created At") {
            @Override public Object getValue(Task task) {
                return task.getCreationDate();
            }
            @Override public void setValue(Task task, Object value) {
                task.setCreationDate((Date) value);
            }
            @Override public Class getColumnClass() {
                return Date.class;
            }
            @Override public int getWidthInCharacters() {
                return 25;
            }
            @Override public int getColumnIndex() {
                return 3;
            }
        },

        COMPLITED_TO("Completed To") {
            @Override public Object getValue(Task task) {
                return task.getCompletionDate();
            }
            @Override public void setValue(Task task, Object value) {
                task.setCompletionDate((Date) value);
            }
            @Override public Class getColumnClass() {
                return Date.class;
            }
            @Override public int getWidthInCharacters() {
                return 25;
            }
            @Override public int getColumnIndex() {
                return 4;
            }
        },

        IS_COMPLITED("Completed?") {
            @Override public Object getValue(Task task) {
                return task.getCompletedFlag();
            }
            @Override public void setValue(Task task, Object value) {
                task.setCompletedFlag((Boolean) value);
            }
            @Override public Class getColumnClass() {
                return Boolean.class;
            }
            @Override public int getWidthInCharacters() {
                return 5;
            }
            @Override public int getColumnIndex() {
                return 5;
            }
        },

        DELETE_ACTION("") {
            @Override public Object getValue(Task task) {
                JButton button = new JButton();
                return button;
            }
            @Override public void setValue(Task task, Object value) {
                return;
            }
            @Override public Class getColumnClass() {
                return JButton.class;
            }
            @Override public int getWidthInCharacters() {
                return 5;
            }
            @Override public int getColumnIndex() {
                return 6;
            }
        };

        
        private String displayName;

        private Column(String displayName) {
            assert displayName != null && displayName.length() > 0;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        /**
         * Return the class of Object returned by this column.
         */
        public Class getColumnClass() {
          return String.class; // Default value
        }

        /**
         * Return the value for this column for the specified
         * person.
         */
        public abstract Object getValue(Task task);

        public abstract void setValue(Task task, Object value);

        public abstract int getColumnIndex();

        /**
         * Return the number of characters needed to display the
         * header and data for this column.
         */
        public abstract int getWidthInCharacters();
    }


    // protected String[]        columnNames = {"Id", "Order", "Text", "Created At", "Complited To", ""};    
    protected Vector<Task>    dataVector  = new Vector<Task>();
    protected Vector<Task>    bufferData;

    private Dbsn              db = new Dbsn("resources/db/todo");

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

    // @Override
    // public int getColumnCount() {
    //     return columnNames.length;
    // }
    public int getColumnCount() {
        return Column.values().length;
    }
    
    @Override
    public int getRowCount() {
        return dataVector.size();
    }

    private Column getColumn(int columnIndex) {
        return Column.values()[columnIndex];
    }

    // @Override
    // public String getColumnName(int column) {
    //     return columnNames[column];
    // }
    @Override
    public String getColumnName(int columnIndex) {
       return getColumn(columnIndex).getDisplayName();
    }
   
    // @Override
    // public boolean isCellEditable(int row, int column) {
    //     if (column == ID_INDEX || column == HIDDEN_INDEX2)  {
    //         return false;
    //     }
    //     return true;
    // }

    @Override
    public boolean isCellEditable(int row, int columnIndex) {
        if (   columnIndex == Column.ID.getColumnIndex()
            || columnIndex == Column.DELETE_ACTION.getColumnIndex()
            || columnIndex == Column.CREATED_AT.getColumnIndex()
            || columnIndex == Column.COMPLITED_TO.getColumnIndex()
        )  {
            return false;
        }
        return true;
    }

    // @Override
    // public Class getColumnClass(int column)
    // {
    //     switch (column) {
    //         case ORDER_INDEX: 
    //             return Integer.class;
    //         case ID_INDEX:
    //             return String.class;
    //         case TEXT_INDEX:
    //             return String.class;
    //         case HIDDEN_INDEX2:
    //             return JButton.class;
    //         case CREATED_AT_INDEX:
    //         case COMPLITED_TO_INDEX:
    //             return Date.class;            
    //         default: 
    //             return Object.class;
    //     }
    // }
    @Override
    public Class getColumnClass(int columnIndex) {
        Column column = getColumn(columnIndex);
        return column.getColumnClass();
    }

    // @Override
    // public Object getValueAt(int row, int column)
    // {
    //     Task task = (Task) dataVector.get(row);
    //     switch (column) {
    //         case ID_INDEX:
    //             return task.getId();
    //         case ORDER_INDEX:
    //             return task.getOrder();
    //         case TEXT_INDEX:
    //             return task.getText();
    //         case CREATED_AT_INDEX:
    //             return task.getCreationDate();
    //         case COMPLITED_TO_INDEX:
    //             return task.getCompletionDate();
    //         case HIDDEN_INDEX2:
    //             JButton button = new JButton();
    //             return button;
    //         default:
    //             return new Object();
    //     }
    // }
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task   task   = getTask(rowIndex);
        Column column = getColumn(columnIndex);
        return column.getValue(task);
    }


    // @Override
    // public void setValueAt(Object value, int row, int column)
    // {
    //     Task task = (Task) dataVector.get(row);
    //     switch (column) {
    //         case ID_INDEX:
    //             task.setId((String) value);
    //             break;
    //         case ORDER_INDEX:
    //             task.setOrder((Integer) value);
    //             break;
    //         case TEXT_INDEX:
    //             task.setText((String) value);
    //             break;
    //         case CREATED_AT_INDEX:
    //             task.setCreationDate((Date) value);
    //             break;
    //         case COMPLITED_TO_INDEX:
    //             task.setCompletionDate((Date) value);
    //             break;
    //         default:
    //             System.out.println("invalid index");
    //     }
    //     fireTableCellUpdated(row, column);
    // }
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Task   task   = getTask(rowIndex);
        Column column = getColumn(columnIndex);
        column.setValue(task, value);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }


    /**
     * [getTask description]
     * @param  rowIndex [description]
     * @return          [description]
     */
    Task getTask(int rowIndex) {
        return (Task) dataVector.get(rowIndex);
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
        if  (  task.getText().trim().isEmpty() 
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
        Task task = new Task();
        dataVector.add(task);
        fireTableRowsInserted(dataVector.size() - 1, dataVector.size() - 1);
    }

    /**
     * [removeRow description]
     * @param row [description]
     */
    public void removeRow(int rowIndex)
    {
        dataVector.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
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
        for (Task t : getRemovedTask()) {
            db.removeTask(t);
        }
        for (Task t : getCreatedTask()) {
            db.addTask(t);
            t.resetChangedFlag();
        }
        for (Task t : getChangedTask()) {
            db.updateTask(t);
            t.resetChangedFlag();
        }
        db.flush();
        bufferData = new Vector<Task>(dataVector);
    }
}