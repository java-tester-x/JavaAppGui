import java.awt.*;
import java.awt.event.*;
import java.awt.Component;
import java.awt.image.BufferedImage;

import static javax.swing.GroupLayout.Alignment.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;


import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import net.sourceforge.jdatepicker.impl.UtilCalendarModel;
import net.sourceforge.jdatepicker.impl.UtilDateModel;


import com.todolist.model.TaskTableModel;
import com.todolist.util.LineNumberTableRowHeader;

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

    private String      imagPath  = "resources/images/";
	
	// Name-constants to define the various dimensions
	public static final int WINDOW_WIDTH  = 640;
	public static final int WINDOW_HEIGHT = 480;

    private Container          mainContentPane;
    private GroupLayout        layout;

    private TaskTableModel     taskTableModel;
    private JTable             taskTable;
    private JPanel             inputPanel;
    private JScrollPane        scroller;
    private JButton            addButton;
    private JButton            saveButton;
    private JButton            refreshButton;
    private BufferedImage      removeButtonIcon;

    private JLabel             findLabel     = new JLabel("Find:");;
    private JTextField         findText      = new JTextField();
    private JButton            findButton    = new JButton("Find");

    public ApplicationWindow(String title)
    {
		mainContentPane = this.getContentPane();

		// Content-pane sets layout
		// cp.setLayout(new ....Layout());        
        layout = new GroupLayout(mainContentPane);
        mainContentPane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
		// Allocate the UI components
        initComponents();   

		// Content-pane adds components
        mainContentPane.add(scroller, BorderLayout.CENTER);   
        mainContentPane.add(inputPanel, BorderLayout.NORTH);   

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
        //
        try {
            removeButtonIcon = ImageIO.read(new File(imagPath+"item-del.png"));
        } catch (IOException e) {}

        //
        taskTableModel = new TaskTableModel();
        taskTableModel.addTableModelListener(new ApplicationWindow.TaskTableModelListener());
        taskTable = new JTable();
        taskTable.setModel(taskTableModel);
        taskTable.setSurrendersFocusOnKeystroke(true);
        taskTable.addMouseListener(new JTableButtonMouseListener(taskTable));

        //
        scroller = new JScrollPane(taskTable);
        taskTable.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));

        //
        LineNumberTableRowHeader tableLineNumber = new LineNumberTableRowHeader(scroller, taskTable);
        tableLineNumber.setBackground(taskTable.getGridColor());
        scroller.setRowHeaderView(tableLineNumber);

        //
        // TableColumn hidden = taskTable.getColumnModel().getColumn(TaskTableModel.ID_INDEX);
        // hidden.setCellRenderer(new HiddenColumnRenderer(TaskTableModel.ID_INDEX));
        TableColumn hidden = taskTable.getColumnModel().getColumn(TaskTableModel.Column.values()[0]);
        hidden.setCellRenderer(new HiddenColumnRenderer(TaskTableModel.Column.ID));
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);

        //
        // TableColumn buttonColumn = taskTable.getColumnModel().getColumn(TaskTableModel.HIDDEN_INDEX2);
        TableColumn buttonColumn = taskTable.getColumnModel().getColumn(TaskTableModel.Column.values()[5]);
        buttonColumn.setMinWidth(20);
        buttonColumn.setPreferredWidth(20);
        buttonColumn.setMaxWidth(20);
        buttonColumn.setCellRenderer(new ButtonColumnRenderer());

        //
        // TableColumn sortColumn = taskTable.getColumnModel().getColumn(TaskTableModel.ORDER_INDEX);
        TableColumn sortColumn = taskTable.getColumnModel().getColumn(TaskTableModel.Column.values()[1]);
        sortColumn.setMinWidth(40);
        sortColumn.setPreferredWidth(40);
        sortColumn.setMaxWidth(40);
        
        //        
        // TableColumn textColumn = taskTable.getColumnModel().getColumn(TaskTableModel.TEXT_INDEX);
        TableColumn textColumn = taskTable.getColumnModel().getColumn(TaskTableModel.Column.values()[2]);
        textColumn.setPreferredWidth(350);
        
        addButton     = new JButton("Add");
        addButton.addActionListener(new AddTaskActionListener());
        saveButton    = new JButton("Save");
        saveButton.addActionListener(new SaveDataActionListener());
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new LoadDataActionListener());

        inputPanel = new JPanel();
        inputPanel.add(addButton);
        inputPanel.add(saveButton);
        inputPanel.add(refreshButton);

        // UtilDateModel   model = new UtilDateModel();
        // model.setDate(1990, 8, 24);
        // model.setSelected(true); 
        // JDatePanelImpl  datePanel  = new JDatePanelImpl(model);
        // JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        // inputPanel.add(datePicker);


        // TableColumn creationDateColumn = taskTable.getColumnModel().getColumn(TaskTableModel.CREATED_AT_INDEX);
        // creationDateColumn.setCellEditor(new MyTableCellEditor());


        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(findLabel)
                .addComponent(findText)
                .addComponent(findButton)
            )
            .addComponent(scroller)
            .addComponent(inputPanel)
        );        
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(findLabel)
                .addComponent(findText)
                .addComponent(findButton)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)                
                .addComponent(scroller)
            )
            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)                
                .addComponent(inputPanel)
            )
        );
    }

    /**
     * 
     */
    class RemoveTaskActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int rowIndex = ApplicationWindow.this.taskTable.getSelectedRow();
            int modelRow = ApplicationWindow.this.taskTable.convertRowIndexToModel(rowIndex);
            ApplicationWindow.this.taskTableModel.removeRow(modelRow);
        }
    };

    /**
     * 
     */
    class AddTaskActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (ApplicationWindow.this.taskTableModel.hasEmptyRow()) {
                return;
            }
            ApplicationWindow.this.taskTableModel.addEmptyRow();                
        }
    }

    /**
     * 
     */
    class SaveDataActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {                
            ApplicationWindow.this.taskTableModel.save();
        }
    }

    /**
     * 
     */
    class LoadDataActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            ApplicationWindow.this.taskTableModel.refreshData();
        }
    }

    
    class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor
    {
        JComponent component = new JTextField();

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex)
        {
            ((JTextField) component).setText((String) value);

            return component;
        }

        public Object getCellEditorValue() {
            return ((JTextField) component).getText();
        }
    }

    /**
     * 
     */
    class HiddenColumnRenderer extends DefaultTableCellRenderer
    {    
        protected int hiddenColumn;

        public HiddenColumnRenderer(int hiddenColumn) {
            this.hiddenColumn = hiddenColumn;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (column == hiddenColumn && hasFocus) {
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
    class ButtonColumnRenderer implements TableCellRenderer
    {        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            JButton button = (JButton) value;
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            }
            else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            
            button.setIcon(new ImageIcon(removeButtonIcon));
            button.setBorder(BorderFactory.createEmptyBorder());
            button.setContentAreaFilled(false);
            return button;  
        }
    }

    /**
     * 
     */
    class JTableButtonMouseListener extends MouseAdapter
    {
        private final JTable table;
        
        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row    = e.getY()/table.getRowHeight(); 

            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    ((JButton)value).addActionListener(new RemoveTaskActionListener());
                    ((JButton)value).doClick();
                }
            }
        }
    }

    /**
     * 
     */
    public class TaskTableModelListener implements TableModelListener
    {
        public void tableChanged(TableModelEvent evt) {
            int column = evt.getColumn();
            int row    = evt.getFirstRow();

            if (evt.getType() == TableModelEvent.INSERT) {                
                System.out.println("INSERT ROW: " + row + " column: " + column);
                taskTable.scrollRectToVisible(taskTable.getCellRect(taskTable.getRowCount()-1, 0, true));
                taskTable.setRowSelectionInterval(row, row);
            }
            else if (evt.getType() == TableModelEvent.UPDATE) {
                System.out.println("UPDATE ROW: " + row + " column: " + column);
                taskTable.setColumnSelectionInterval(column + 1, column + 1);
            }
            else if (evt.getType() == TableModelEvent.DELETE) {
                System.out.println("DELETE row: " + row + " column: " + column);
                if (row - 1 >= 0) {
                    taskTable.setRowSelectionInterval(row - 1, row - 1);
                }
            }
        }

    }

}