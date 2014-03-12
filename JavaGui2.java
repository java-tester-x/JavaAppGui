import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

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

    private DefaultTableModel  taskTableModel;
    private JTable             taskTable;
    private JButton            addButton;
    private JButton            removeButton;
    private JPanel             inputPanel;


    public ApplicationWindow(String title)
    {
		Container contentPane = this.getContentPane();

		// Content-pane sets layout
		// cp.setLayout(new ....Layout());

		// Allocate the UI components
        initComponents();   

		// Content-pane adds components
        contentPane.add(new JScrollPane(taskTable), BorderLayout.CENTER);   
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
        Dbsn db = Dbsn.getInstance();
        db.connectToDbsn("resources/TestDBSN");
        Vector<Vector<Object>> data = db.loadData();
        db.disconnectFromDbsn();

        taskTable = new JTable(taskTableModel);
        taskTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);


        addButton    = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String[] task = {"", "", "", ""};
                taskTableModel.addRow(task);                
            }
        });

        removeButton = new JButton("Remove Task");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                int rowIndex = taskTable.getSelectedRow();
                taskTableModel.removeRow(rowIndex);
            }
        });

        inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
    }

    // private DefaultTableModel buildTableModelFromDbsn()
    // {
    //     String[] columns = {"Id", "Text", "Created At", "Complited To"};
    //     Vector<String> columnNames = new Vector<String>();
    //     Vector<Vector<Object>> data = new Vector<Vector<Object>>();

    //     // names of columns        
    //     int columnCount = columns.length;
    //     for (int i = 0; i < columnCount; i++) {
    //         columnNames.add(columns[i]);
    //     }

    //     // data of the table        
    //     int rowCount = DbsnLibrary.INSTANCE.countFragm(dbh);
    //     for(int i = 0; i < rowCount; i++)
    //     {
    //         byte[] fragm = new byte[32567];
    //         DbsnLibrary.INSTANCE.setNom(dbh, i);
    //         DbsnLibrary.INSTANCE.getFragm(dbh, fragm, fragm.length);

    //         Vector<Object> vector = new Vector<Object>();
    //         vector.add(i);
    //         vector.add(new String(fragm).trim());
    //         vector.add(null);
    //         vector.add(null);

    //         data.add(vector);
    //     }

    //     return new DefaultTableModel(data, columnNames);
    // }


    public class Dbsn {
        private static Dbsn instance;

        public static synchronized Dbsn getInstance() {
            if (instance == null) {
                instance = new Dbsn();
            }
            return instance;
        }


        private int dbh;

        public int connectToDbsn(String dbsnPath) {
            return DbsnLibrary.INSTANCE.openDBSN(dbsnPath);
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

        public Vector<Vector<Object>> loadData()
        {
            int rowCount = DbsnLibrary.INSTANCE.countFragm(dbh);

            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            for(int i = 0; i < rowCount; i++)
            {
                byte[] fragm = new byte[32567];
                DbsnLibrary.INSTANCE.setNom(dbh, i);
                DbsnLibrary.INSTANCE.getFragm(dbh, fragm, fragm.length);

                Vector<Object> vector = new Vector<Object>();
                vector.add(i);
                vector.add(new String(fragm).trim());
                vector.add(null);
                vector.add(null);

                data.add(vector);
            }

            return data;
        } 
    }

}
