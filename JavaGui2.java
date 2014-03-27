import java.awt.*;
import java.awt.event.*;
import java.awt.Component;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;


import java.util.Vector;

import com.todolist.util.DbsnLibrary;
import com.todolist.model.Task;
import com.todolist.model.TaskTableModel;

/**
 * Main apllication file.
 */
public class JavaGui2 {

	public static void main(String[] args)
	{
        final String text = "Simple Todo List";

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ApplicationWindow wnd = new ApplicationWindow(text);
                wnd.setVisible(true);                
            }
        });
	}
}


class ApplicationWindow extends JFrame {
	
	// Name-constants to define the various dimensions
	public static final int WINDOW_WIDTH  = 640;
	public static final int WINDOW_HEIGHT = 480;

    private JMenu   mainMenu  = null;
    private JPanel  mainPanel = null;

    private TaskTableModel     taskTableModel;
    private JTable             taskTable;
    private Vector<Task>       taskTableData;

    private JPanel             inputPanel;
    private JScrollPane        scroller;
    private JButton            addButton;
    private JButton            removeButton;
    private JButton            saveButton;
    private JButton            refreshButton;

    private Dbsn               db = new Dbsn();
    

    public ApplicationWindow(String title)
    {
		Container contentPane = this.getContentPane();

		// Content-pane sets layout
		// cp.setLayout(new ....Layout());

		// Allocate the UI components
        initComponents();   

		// Content-pane adds components
        contentPane.add(scroller, BorderLayout.CENTER);   
        contentPane.add(inputPanel, BorderLayout.NORTH);   

		// Source object adds listener
		// .....
      
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);       
        setTitle(title);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);        
    }

    /**
     * [initComponents description]
     */
    private void initComponents()
    {
        taskTableData  = loadDataFromDbsn();
        taskTableModel = new TaskTableModel(taskTableData);
        taskTableModel.addTableModelListener(new ApplicationWindow.TaskTableModelListener());
        taskTable = new JTable();
        taskTable.setModel(taskTableModel);
        taskTable.setSurrendersFocusOnKeystroke(true);

        //TODO: разобраться!
        //
        //BufferedImage buttonIcon = ImageIO.read(new File("buttonIconPath"));
        //button = new JButton(new ImageIcon(buttonIcon));
        //button.setBorder(BorderFactory.createEmptyBorder());
        //button.setContentAreaFilled(false);

        scroller = new JScrollPane(taskTable);
        taskTable.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        TableColumn hidden = taskTable.getColumnModel().getColumn(TaskTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new TaskRenderer(TaskTableModel.HIDDEN_INDEX));


        addButton    = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if ( ! taskTableModel.hasEmptyRow()) {
                    taskTableModel.addEmptyRow();
                }                
            }
        });

        removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int rowIndex = taskTable.getSelectedRow();
                int modelRow = taskTable.convertRowIndexToModel(rowIndex);
                taskTableModel.removeRow(modelRow);
            }
        });

        saveButton = new JButton("Save changes");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {                
                saveDataToDbsn(
                        taskTableModel.getCreatedTask()
                    ,   taskTableModel.getChangedTask()
                    ,   taskTableModel.getRemovedTask() 
                );
            }
        });

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                taskTableData = loadDataFromDbsn();
                taskTableModel.setData(taskTableData);
            }
        });

        inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(saveButton);
        inputPanel.add(refreshButton);
    }

    /**
     * [loadDataFromDbsn description]
     * @return [description]
     */
    private Vector<Task> loadDataFromDbsn() {
        db.connectToDbsn("resources/TestDBSN");
        Vector<Task> data = db.loadData();
        db.disconnectFromDbsn();
        return data;
    }

    /**
     * [saveDataToDbsn description]
     * @param changedTask [description]
     */
    private void saveDataToDbsn(Vector<Task> createdTask
                              , Vector<Task> changedTask
                              , Vector<Task> removedTask)
    {
        db.connectToDbsn("resources/TestDBSN");
        for (Task t : changedTask) {
            db.updateTask(t);
            t.resetChangedFlag();
        }
        for (Task t : removedTask) {
            db.removeTask(t);
        }
        for (Task t : createdTask) {
            db.addTask(t);
            t.resetChangedFlag();
        }
        db.disconnectFromDbsn();
    }


    /**
     * 
     */
    class TaskRenderer extends DefaultTableCellRenderer {
        
        protected int taskColumn;

        public TaskRenderer(int taskColumn) {
            this.taskColumn = taskColumn;
        }

        public Component getTableCellRendererComponent( JTable  table
                                                      , Object  value
                                                      , boolean isSelected
                                                      , boolean hasFocus
                                                      , int     row
                                                      , int     column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == taskColumn && hasFocus) {
                if  (
                        (ApplicationWindow.this.taskTableModel.getRowCount() - 1) == row 
                    &&
                        ! ApplicationWindow.this.taskTableModel.hasEmptyRow()
                    )
                {
                    ApplicationWindow.this.taskTableModel.addEmptyRow();
                }
                highlightLastRow(row);
            }

            return c;
        }

        /**
         * [highlightLastRow description]
         * @param row [description]
         */
        public void highlightLastRow(int row) {
            int lastrow = ApplicationWindow.this.taskTableModel.getRowCount();
            if (row == lastrow - 1) {
                ApplicationWindow.this.taskTable.setRowSelectionInterval(lastrow - 1, lastrow - 1);
            } else {
                ApplicationWindow.this.taskTable.setRowSelectionInterval(row + 1, row + 1);
            }
            ApplicationWindow.this.taskTable.setColumnSelectionInterval(0, 0);
        }

    }

    /**
     * 
     */
    public class TaskTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            int column = evt.getColumn();
            int row    = evt.getFirstRow();

            if (evt.getType() == TableModelEvent.INSERT) {                
                System.out.println("INSERT ROW: " + row + " column: " + column);
                taskTable.scrollRectToVisible(taskTable.getCellRect(taskTable.getRowCount()-1, 0, true));
            }
            else if (evt.getType() == TableModelEvent.UPDATE) {
                System.out.println("UPDATE ROW: " + row + " column: " + column);
                taskTable.setColumnSelectionInterval(column + 1, column + 1);
            }
            else if (evt.getType() == TableModelEvent.DELETE) {
                System.out.println("DELETE row: " + row + " column: " + column);
                row = (row == 0)  ? row : (row - 1);
            }
            taskTable.setRowSelectionInterval(row, row);
        }

    }


    /**
     * 
     */
    public class Dbsn {

        private int dbh;

        public void connectToDbsn(String dbsnPath) {
            dbh = DbsnLibrary.INSTANCE.openDBSN(dbsnPath);
        }

        public void disconnectFromDbsn() {
            DbsnLibrary.INSTANCE.flushDBSN(dbh);
            DbsnLibrary.INSTANCE.closeDBSN(dbh);
        }

        public void addTask(Task task) {
            DbsnLibrary.INSTANCE.addFragm(dbh, task.getText());
        }

        public void updateTask(Task task) {
            DbsnLibrary.INSTANCE.setNom(dbh, task.getId());
            DbsnLibrary.INSTANCE.setFragm(dbh, task.getText());
        }

        public void removeTask(Task task) {
            DbsnLibrary.INSTANCE.setNom(dbh, task.getId());
            DbsnLibrary.INSTANCE.cutFragm(dbh);
        }

        public Vector<Task> loadData()
        {
            int rowCount = DbsnLibrary.INSTANCE.countFragm(dbh);
            Vector<Task> data = new Vector<Task>();
            for(int i = 1; i <= rowCount; i++)
            {
                byte[] fragm = new byte[32567];
                DbsnLibrary.INSTANCE.setNom(dbh, i);
                DbsnLibrary.INSTANCE.getFragm(dbh, fragm, fragm.length);
                String text = new String(fragm).trim();
                Task aTask  = new Task(i, 0, text);
                data.add(aTask);
            }
            return data;
        } 
    }

}
