package app.ui.menu;

import app.service.DoctorService;
import app.service.LogService;
import app.service.PatientService;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import app.ui.users.Doctor;
import app.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class used to update the doctor a patient is assigned to.
 */
public class AssignDoctor extends JPanel implements ActionListener {
    public JComboBox<String> patientDropdown;
    public JComboBox<String> doctorDropdown;
    private final Main main;
    private final Doctor doctor;
    private final JPanel switchPanel;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final LogService logService;
    private final CardLayout cardLayout;
    private JButton assignBtn, backBtn;


    public AssignDoctor(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        patientService = new PatientService();
        doctorService = new DoctorService();
        logService = new LogService();

        createPanel();
    }


    /**
     * Creates JPanel using MigLayout to structure the header, table and buttons.
     */
    private void createPanel() {
        setLayout(new MigLayout("align center, insets 20", "[center]", "[center][center]"));

        JPanel assignPanel = new JPanel(new MigLayout("wrap, insets 15 45, gapy 10", "fill, 250:280"));
        assignPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("Assign New Doctor");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        assignPanel.add(title, "span, gapx 55, gapy 0, height 30");

        //Store all patients in an array so that they can be retrieved and stored inside ComboBox.
        String[] patients = patientService.getMyPatients(doctor.doctorId());
        String[] copyPatients = new String[patients.length + 1];
        copyPatients[0] = "Select Patient";
        System.arraycopy(patients, 0, copyPatients, 1, patients.length);

        patientDropdown = new JComboBox<>(copyPatients);
        assignPanel.add(patientDropdown, "gapy 20, height 30");

        //Store all doctors in an array so that they can be retrieved and stored inside ComboBox.
        String[] doctors = doctorService.getAllDoctors();
        String[] copyDoctors = new String[doctors.length + 1];
        copyDoctors[0] = "Select Doctor";
        System.arraycopy(doctors, 0, copyDoctors, 1, doctors.length);

        doctorDropdown = new JComboBox<>(copyDoctors);
        assignPanel.add(doctorDropdown, "height 30");

        assignBtn = new JButton("Assign");
        assignBtn.addActionListener(this);
        assignPanel.add(assignBtn, "gapy 10, height 30");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(150, 25));

        add(assignPanel, "cell 0 0, gapbottom 50");
        add(backBtn, "cell 0 1");
    }

    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and updating patient doctor via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == assignBtn) {

            //Different if statements to check the missing fields that the doctor has not filled out.
            if (patientDropdown.getSelectedIndex() == 0 && doctorDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Fill in the Fields");
                return;
            }
            else if (patientDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a Patient");
                return;
            }
            else if (doctorDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Please Select a Doctor");
                return;
            }

            String patientSelection = (String) patientDropdown.getSelectedItem();
            String doctorSelection = (String) doctorDropdown.getSelectedItem();

            String patientName = patientSelection.split(" ")[1] + " " + patientSelection.split(" ")[2];
            String doctorName = doctorSelection.split(" ")[1] + " " + doctorSelection.split(" ")[2];

            int newChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to assign " + doctorName + " to " + patientName,"Confirm Assign", JOptionPane.YES_NO_OPTION);
            if (newChoice == 0) {
                //String slicing and parsing to get the Id of the patient and the doctor from the users selected in ComboBoxes.
                int patientId = Integer.parseInt(patientSelection.split(" ")[0]);
                int doctorId = Integer.parseInt(doctorSelection.split(" ")[0]);

                //SQL Query to update patient's new doctor based on parsed data.
                patientService.updatePatientDoctor(patientId, doctorId);
                logService.addLog(doctor.doctorId(), "Re-assigned Patient to Doctor");
                JOptionPane.showMessageDialog(this, "Patient Doctor Updated");
                cardLayout.show(switchPanel, "doctor");
            }
        }

        //If back button is selected, doctor returns back to DoctorPage (Home Page)
        else if (e.getSource() == backBtn) {
            main.showDoctorPanel(doctor);
        }
    }
}

