import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Main apllication file.
 */
public class JavaGui {

	public static void main(String[] args)
	{
        final String text = "The-Simple-Application";

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
	public static final int WINDOW_WIDTH  = 300;
	public static final int WINDOW_HEIGHT = 150;

    JMenu   mainMenu  = null;
    JPanel  mainPanel = null;

    public ApplicationWindow(String title)
    {
		Container cp = this.getContentPane();

		// Content-pane sets layout
		// cp.setLayout(new ....Layout());

		// Allocate the UI components
		// .....

		// Content-pane adds components
		// cp.add(....)

		// Source object adds listener
		// .....
      
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);       
        setTitle(title);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);        
    }
}