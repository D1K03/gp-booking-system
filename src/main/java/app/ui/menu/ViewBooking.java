package app.ui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import app.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import app.service.BookingService;
import app.ui.tables.BookingTable;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

/**
 * This class allows a doctor to view their bookings from a certain month and year in a table
 */
public class ViewBooking extends JPanel implements ActionListener {
    private final Main main;
    private final JPanel switchPanel;
    private final CardLayout cardLayout;
    private BookingTable BTable;
    private final Doctor doctor;
    private JButton backBtn, tableBtn;
    private JLabel headerLabel;
    private final BookingService bookingService;
    private int chosenMonth, chosenYear;

    // Constructor: This is run when a new ViewBooking screen is created
    public ViewBooking(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, int chosenMonth, int chosenYear, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        this.chosenMonth = chosenMonth;
        this.chosenYear = chosenYear;
        bookingService = new BookingService();

        createPanel();
        loadBookingData();
    }

    /**
     * This method sets up the design/layout of the panel. It uses MigLayout to organise components, adds buttons, a table  and a header.
     */
    private void createPanel () {
        setLayout(new MigLayout("fill, insets 20", "[grow, center]", "[][grow][]"));

        // Apply a modern rounded look
        putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%)"
        );

        // Title at the top
        headerLabel = new JLabel("My Bookings");
        headerLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");
        add(headerLabel, "cell 0 0, wrap");

        //Creates and add the table to show booking info
        BTable = new BookingTable();
        BTable.setPreferredScrollableViewportSize(new Dimension(1000, 600));
        JScrollPane scrollPane = new JScrollPane(BTable);
        add(scrollPane, "cell 0 1, grow, push, gapy 20");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(100, 25));
        add(backBtn, "cell 0 2, gaptop 20, wrap");
    }

    /**
     * Loads booking data into the table
     */
    private void loadBookingData() {
        List<String[]> storedBookings = bookingService.getDoctorBookings(doctor.doctorId(), chosenMonth + 1, chosenYear);

        //Adds data into the column for each row in the JTable
        if (storedBookings != null) {
            DefaultTableModel model = BTable.getModel();
            model.setRowCount(0);
            for (String[] patient : storedBookings) {
                String[] row = {patient[0], patient[1], patient[2], patient[3], patient[4], patient[5]};
                model.addRow(row);
            }
        }
    }

    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and updating JTable data.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            cardLayout.show(switchPanel, "bookings");
        }
    }
}
