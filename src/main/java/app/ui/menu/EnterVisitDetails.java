package app.ui.menu;

import app.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import app.service.LogService;
import app.service.PatientService;
import app.service.VisitService;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import com.github.lgooddatepicker.components.DateTimePicker;
import net.miginfocom.swing.MigLayout;

/**
 * Class to allow doctors to track user visit history by entering details of their visit and prescription.
 */
public class EnterVisitDetails extends JPanel implements ActionListener {
    private final CardLayout cardLayout;
    private final JPanel switchPanel;
    private final Doctor doctor;
    private final Main main;
    private final PatientService patientService;
    private final LogService logService;
    private final VisitService visitService;
    private JComboBox<String> patientDropdown;
    private DateTimePicker dateArrival;
    private JTextArea notesTextArea;
    private JTextArea messageTextArea;
    private JButton enterButton, backBtn;


    public EnterVisitDetails(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.doctor = doctor;
        this.main = main;
        logService = new LogService();
        visitService = new VisitService();
        patientService = new PatientService();
        createPanel();
    }

    /**
     * Creates JPanel using MigLayout to structure the header, table and buttons.
     */
    private void createPanel() {
        setLayout(new MigLayout("align center, insets 20", "[center]", "[center][center]"));
        JPanel enterPanel = new JPanel(new MigLayout("wrap, insets 15 45", "fill, 250:280"));
        enterPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("Enter Visit Details");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        enterPanel.add(title,"gapx 50, gapbottom 20");

        // Retrieves patients to list in a ComboBox, creates a list with an extra index to store placeholder text.
        String[] patients = patientService.getMyPatients(doctor.doctorId());
        String[] copyPatients = new String[patients.length + 1];
        copyPatients[0] = "Select Patient";
        System.arraycopy(patients, 0, copyPatients, 1, patients.length);

        patientDropdown = new JComboBox<>(copyPatients);
        enterPanel.add(patientDropdown, "height 30, gapbottom 10");

        JLabel visitDate = new JLabel("Visit Date:");
        enterPanel.add(visitDate);

        dateArrival = new DateTimePicker();
        enterPanel.add(dateArrival, "height 30, gapbottom 10");

        JLabel messageTitle = new JLabel("Prescription:");
        enterPanel.add(messageTitle);

        //Allows doctor to enter new prescription details for the patient.
        messageTextArea = new JTextArea(5, 20);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        enterPanel.add(new JScrollPane(messageTextArea), "growx, gapbottom 10");

        JLabel notesTitle = new JLabel("Additional Notes:");
        enterPanel.add(notesTitle);

        notesTextArea = new JTextArea(3, 20);
        notesTextArea.setLineWrap(true);
        notesTextArea.setWrapStyleWord(true);
        enterPanel.add(new JScrollPane(notesTextArea), "growx, gapbottom 15");

        enterButton = new JButton("Enter Details");
        enterButton.addActionListener(this);
        enterPanel.add(enterButton, "span, align center, height 30");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(150, 25));

        add(enterPanel, "cell 0 0, gapbottom 50");
        add(backBtn, "cell 0 1");
    }


    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and adding new visit entries via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            LocalDateTime localDate = dateArrival.getDateTimeStrict();

            //Different if statements to check the missing fields that the doctor has not filled out.
            if (patientDropdown.getSelectedIndex() == 0 && localDate == null) {
                JOptionPane.showMessageDialog(this, "Please Fill in the Fields");
                return;
            }
            else if (patientDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a Patient");
                return;
            }
            else if (localDate == null) {
                JOptionPane.showMessageDialog(this, "Please Enter a Valid Visit Date");
                return;
            }

            String patientText = (String) patientDropdown.getSelectedItem();
            String patientName = patientText.split(" ")[1] + " " + patientText.split(" ")[2];


            int newChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Add Visit Details and Prescriptions for " + patientName, "Confirm Add", JOptionPane.YES_NO_OPTION);
            if (newChoice == 0) {
                //Gets the first part of patientText representing the Id then parses it to an Integer.
                int patientId = Integer.parseInt(patientText.split(" ")[0]);

                String prescribeText = messageTextArea.getText();
                String notesText = notesTextArea.getText();

                // Inserts visit and prescription details into the Visits table.
                visitService.insertVisit(patientId, doctor.doctorId(), notesText, prescribeText, localDate);
                logService.addLog(doctor.doctorId(), "Entered new Visit Details");
                JOptionPane.showMessageDialog(this, "Visit Details and Prescriptions for " + patientName + " has been added.");
                cardLayout.show(switchPanel, "doctor");
            }
        }

        else if (e.getSource() == backBtn) {
            main.showDoctorPanel(doctor);
        }
    }
}

