package app.ui.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Class to store visit information.
 */
public class VisitTable extends JTable {
    private DefaultTableModel model;

    public VisitTable() {
        createVTable();
    }

    /**
     * Creates Template for VisitTable
     * Headers are set for the VisitTable to store all the relevant visit details.
     */
    private void createVTable() {
        String[] columnHeader = {"VisitID", "PatientID", "Full Name", "Notes", "Prescriptions", "Visit Date"};
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
