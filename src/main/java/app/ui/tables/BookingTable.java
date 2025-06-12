package app.ui.tables;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Class to store user information.
 */
public class BookingTable extends JTable {
    private DefaultTableModel model;

    public BookingTable() {
        createBTable();
    }

    /**
     * Creates Template for BookingTable
     * Headers are set for the BookingTable to store all the relevant booking records.
     */
    private void createBTable() {
        String[] columnHeader = {"BookingID", "DoctorID", "PatientID", "Appointment Date", "Status", "Booking Date"};
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
