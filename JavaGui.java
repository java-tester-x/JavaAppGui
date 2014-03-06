import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.todolist.model.*;
import com.todolist.view.*;
import com.todolist.controller.*;

/**
 * Main apllication file.
 */
public class JavaGui {

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

    JMenu   mainMenu  = null;
    JPanel  mainPanel = null;

    public ApplicationWindow(String title)
    {
		Container contentPane = this.getContentPane();

		// Content-pane sets layout
		// cp.setLayout(new ....Layout());
        contentPane.setLayout(new GridLayout(3, 1));

		// Allocate the UI components
		// .....
        Task aTask = new Task(1, 0, "String text");
        TaskTextView taskTextView = new TaskTextView(aTask);
        TaskController taskController = new TaskController(aTask);

        JPanel taskPanel = new JPanel();
        taskPanel.setBorder(new TitledBorder("Task #" + aTask.getId()));
        taskPanel.add(taskController);
        taskPanel.add(taskTextView);

		// Content-pane adds components
		// cp.add(....)
        contentPane.add(taskPanel);

		// Source object adds listener
		// .....

      
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);       
        setTitle(title);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);        
    }
}