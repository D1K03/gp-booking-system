package app.ui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import app.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import app.service.PatientService;
import app.service.UserService;
import app.ui.tables.UserTable;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

/**
 * This class allows a doctor to view  patients in a table - either on their own assigned patients  or all patients in the system. The user can toggle between  two views
 */
public class ViewPatients extends JPanel implements ActionListener {
    private final Main main;
    private final JPanel switchPanel;
    private final CardLayout cardLayout;
    private UserTable UTable;
    private final Doctor doctor;
    private JButton backBtn, tableBtn;
    private final UserService userService;
    private JLabel headerLabel;
    private final PatientService patientService;

    // Constructor: This is run when a new ViewPatients screen is created
    public ViewPatients(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        userService = new UserService();
        patientService = new PatientService();

        createPanel();
        loadUserData("Show My Patients");
    }

    /**
     * This method sets up the design/layout of the panel. It uses MigLayout to organise components, adds buttons, a table  and a header.
     */
    private void createPanel () {
        setLayout(new MigLayout("fill, insets 20", "[grow, center]", "[][grow][][]"));

        // Apply a modern rounded look
        putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%)"
        );

        // Title at the top
        headerLabel = new JLabel(" My Patients");
        headerLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");
        add(headerLabel, "cell 0 0, wrap");

        //Creates and add the table to show patient info
        UTable = new UserTable();
        UTable.setPreferredScrollableViewportSize(new Dimension(1000, 600));
        JScrollPane scrollPane = new JScrollPane(UTable);
        add(scrollPane, "cell 0 1, grow, push, gapy 20");

        tableBtn = new JButton("Show All Patients");
        tableBtn.addActionListener(this);
        tableBtn.setFocusable(false);
        tableBtn.setPreferredSize(new Dimension(100, 50));
        add(tableBtn, "cell 0 2, align right, wrap, gapy 20");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(100, 25));
        add(backBtn, "cell 0 3, wrap");
    }

    /**
     * Loads patient data into the table depending on the button text.
     * @param currText this tells the method whether to load "My patients" or "All patients"
     */
    private void loadUserData(String currText) {
        List<String[]> storedPatients = null;

        //Verifies the current state of the JTable before loading new data
        if (currText.equals("Show All Patients")) {
            storedPatients = userService.getAllUsers();
        } else {
            storedPatients = patientService.getMyUsers(doctor.doctorId());
        }

        //Adds data into the column for each row in the JTable
        if (storedPatients != null) {
            DefaultTableModel model = UTable.getModel();
            model.setRowCount(0);
            for (String[] patient : storedPatients) {
                String fullAddress = patient[7] + ", " + patient[8] + ", " + patient[9];
                String[] row = {patient[0], patient[1], patient[2], patient[3], patient[4], patient[5], patient[6], fullAddress};
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
            main.showDoctorPanel(doctor);
        }

        // Handles toggling between views
        else if (e.getSource() == tableBtn) {
            if (tableBtn.getText().equals("Show All Patients")) {
                loadUserData("Show All Patients");
                headerLabel.setText("All Patients");
                tableBtn.setText("Show My Patients");
            } else {
                loadUserData("Show My Patients");
                headerLabel.setText("My Patients");
                tableBtn.setText("Show All Patients");
            }
        }
    }
}
