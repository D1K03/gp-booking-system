package app.ui.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Class to store user information.
 */
public class UserTable extends JTable {
    private DefaultTableModel model;

    public UserTable() {
        createUTable();
    }

    /**
     * Creates Template for UserTable
     * Headers are set for the UserTable to store all the relevant patient information.
     */
    private void createUTable() {
        String[] columnHeader = {"UserID", "User", "First Name", "Last Name", "Email Address", "Date Of Birth", "Phone Number", "Home Address"};
        model = new DefaultTableModel(null, columnHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        setModel(model);
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
