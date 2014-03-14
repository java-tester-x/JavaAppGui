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

    private void initComponents()
    {
        Dbsn db = new Dbsn();
        db.connectToDbsn("resources/TestDBSN");
        taskTableData = db.loadData();
        db.disconnectFromDbsn();

        taskTableModel = new TaskTableModel(taskTableData);
        taskTableModel.addTableModelListener(new ApplicationWindow.TaskTableModelListener());
        taskTable = new JTable();
        taskTable.setModel(taskTableModel);
        taskTable.setSurrendersFocusOnKeystroke(true);
        if (!taskTableModel.hasEmptyRow()) {
            taskTableModel.addEmptyRow();
        }

        scroller = new javax.swing.JScrollPane(taskTable);
        taskTable.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        TableColumn hidden = taskTable.getColumnModel().getColumn(TaskTableModel.HIDDEN_INDEX);
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new TaskRenderer(TaskTableModel.HIDDEN_INDEX));


        addButton    = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //String[] task = {"", "", "", ""};
                //taskTableModel.addRow(task);                
            }
        });

        removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //int rowIndex = taskTable.getSelectedRow();
                //taskTableModel.removeRow(rowIndex);
            }
        });

        inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
    }

    public void highlightLastRow(int row) {
        int lastrow = taskTableModel.getRowCount();
        if (row == lastrow - 1) {
            taskTable.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            taskTable.setRowSelectionInterval(row + 1, row + 1);
        }

        taskTable.setColumnSelectionInterval(0, 0);
    }


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

    }


    public class TaskTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent evt) {
            if (evt.getType() == TableModelEvent.UPDATE) {
                int column = evt.getColumn();
                int row = evt.getFirstRow();
                System.out.println("row: " + row + " column: " + column);
                taskTable.setColumnSelectionInterval(column + 1, column + 1);
                taskTable.setRowSelectionInterval(row, row);
            }
        }

    }


    public class Dbsn {
        // private static Dbsn instance;

        // public static synchronized Dbsn getInstance() {
        //     if (instance == null) {
        //         instance = new Dbsn();
        //     }
        //     return instance;
        // }


        private int dbh;

        public void connectToDbsn(String dbsnPath) {
            dbh = DbsnLibrary.INSTANCE.openDBSN(dbsnPath);
        }

        public void removeTaskFromDbsn(int taskId) {
            DbsnLibrary.INSTANCE.setNom(dbh, taskId);
            DbsnLibrary.INSTANCE.delFragm(dbh);
        }

        public boolean isConnectedDbsn() {
            return dbh > 0;
        }

        public void disconnectFromDbsn() {
            DbsnLibrary.INSTANCE.flushDBSN(dbh);
            DbsnLibrary.INSTANCE.closeDBSN(dbh);
        }

        public Vector<Task> loadData()
        {
            int rowCount = DbsnLibrary.INSTANCE.countFragm(dbh);

            Vector<Task> data = new Vector<Task>();
            for(int i = 0; i < rowCount; i++)
            {
                byte[] fragm = new byte[32567];
                DbsnLibrary.INSTANCE.setNom(dbh, i);
                DbsnLibrary.INSTANCE.getFragm(dbh, fragm, fragm.length);

                Task aTask = new Task(i, 0, new String(fragm).trim());
                data.add(aTask);
            }

            return data;
        } 
    }

}
