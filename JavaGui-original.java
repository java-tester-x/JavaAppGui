import javax.swing.*;
import javax.swing.JFrame;

import java.awt.event.*;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.CheckboxMenuItem;
import java.awt.PopupMenu;


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

    public ApplicationWindow(String text)
    {
        super(text);

        //
        configSystemTray();

        //
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ApplicationWindow.this.setVisible(false);
                ApplicationWindow.this.dispose();
            }
        });

        //
        final JButton btn = new JButton(text);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ApplicationWindow.this, "Button Pressed", "Hey", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        setLayout(new FlowLayout());
        add(btn);
        pack();

        setLocationRelativeTo(null);
        setSize(new Dimension(162, 100));
    }

    private void configSystemTray() {
    	 // Make a nice icon
        ImageIcon img = new ImageIcon(this.getClass().getClassLoader().getResource("resources/bulb.gif"));

        // Make a TrayIcon with the image
        final PopupMenu popup    = new PopupMenu();
        final TrayIcon trayIcon  = new TrayIcon(img.getImage());
        final SystemTray sysTray = SystemTray.getSystemTray();
       
        // Create a pop-up menu components
        CheckboxMenuItem cb1 = new CheckboxMenuItem("Set auto size");
        CheckboxMenuItem cb2 = new CheckboxMenuItem("Set tooltip");
        
        Menu displayMenu     = new Menu("Display");
        MenuItem aboutItem   = new MenuItem("About");
        MenuItem errorItem   = new MenuItem("Error");
        MenuItem warningItem = new MenuItem("Warning");
        MenuItem infoItem    = new MenuItem("Info");
        MenuItem noneItem    = new MenuItem("None");

        MenuItem exitItem    = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sysTray.remove(trayIcon);
                System.exit(0);
            }
        });
       
        //Add components to pop-up menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(cb1);
        popup.add(cb2);
        popup.addSeparator();
        popup.add(displayMenu);
        displayMenu.add(errorItem);
        displayMenu.add(warningItem);
        displayMenu.add(infoItem);
        displayMenu.add(noneItem);
        popup.add(exitItem);        
       
        trayIcon.setPopupMenu(popup);

        try {
            sysTray.add(trayIcon);
        }
        catch(AWTException e) {
            e.printStackTrace();
           System.out.println("System Tray unsupported!");
        }
    }
}

