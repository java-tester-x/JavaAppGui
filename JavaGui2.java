import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

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

    private void initComponents() {
        taskTableModel = new DefaultTableModel();
        taskTableModel.addColumn("Id");
        taskTableModel.addColumn("Text");
        taskTableModel.addColumn("Created At");
        taskTableModel.addColumn("Complited To");

        /*
        String[] task01 = {"1", "Task 01", "07.03.2014", ""};
        taskTableModel.addRow(task01);
        String[] task02 = {"2", "Task 02", "07.03.2014", ""};
        taskTableModel.addRow(task02);
        String[] task03 = {"3", "Task 03", "07.03.2014", ""};
        taskTableModel.addRow(task03);
        String[] task04 = {"4", "Task 04", "07.03.2014", ""};
        taskTableModel.addRow(task04);
        String[] task05 = {"5", "Task 05", "07.03.2014", ""};
        taskTableModel.addRow(task05);
        */

        String[] tasks = getAllTasksFromDbsn("TestDBSN");
        for(int i = 0; i < tasks.length; i++) {
            String[] taskXX = {Integer.toString(i), tasks[i], "", ""};
            taskTableModel.addRow(taskXX);            
        }

        taskTable = new JTable(taskTableModel);

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
                taskTableModel.removeRow(taskTable.getSelectedRow());            
            }
        });

        inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
    }

    private String[] getAllTasksFromDbsn(String dbsnName)
    {
        int dbh = DbsnLibrary.INSTANCE.openDBSN(dbsnName);
        if (dbh < 0) {
            return new String[] {};
        }

        int retcode;

        int rowCount = 0;
        do {
            rowCount++;
        } while (0 != DbsnLibrary.INSTANCE.setNom(dbh, rowCount));

        byte[]   fragm = new byte[32567];
        String[] tasks = new String[rowCount];
        for(int i = 0; i < tasks.length; i++)
        {
            retcode = DbsnLibrary.INSTANCE.setNom(dbh, i);
            DbsnLibrary.INSTANCE.getFragm(dbh, fragm, fragm.length);
            tasks[i] = new String(fragm).trim();
            tasks[i] += Integer.toString(retcode);
        }

        retcode = DbsnLibrary.INSTANCE.flushDBSN(dbh);
        retcode = DbsnLibrary.INSTANCE.closeDBSN(dbh);

        return tasks;     
    }

    public interface DbsnLibrary extends Library {        
        DbsnLibrary INSTANCE = (DbsnLibrary) Native.loadLibrary("D:/users/vano/JavaAppGui/lib/dbsn", DbsnLibrary.class);

        int createDBSN    (String fil_name);
        int openDBSN      (String fil_name);
        int flushDBSN     (int dbhadr); 
        int closeDBSN     (int dbhadr);
        int addFragm      (int dbhadr, String fragm, int sys_tag, int tag);    
        int getFragm      (int dbhadr, byte[] fragm, int bufsize);
        int setNom        (int dbhadr, int new_nom);
        int countRef      (int fhr);
    }
}
