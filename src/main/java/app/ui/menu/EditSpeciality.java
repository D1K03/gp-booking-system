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
 * Class used to edit the speciality of the doctor.
 */
public class EditSpeciality extends JPanel implements ActionListener {
    private final Main main;
    private final Doctor doctor;
    private final JPanel switchPanel;
    private final DoctorService doctorService;
    private final LogService logService;
    private final CardLayout cardLayout;
    private JTextField docSpeciality;
    private JButton updateBtn, backBtn;


    public EditSpeciality(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        logService = new LogService();
        doctorService = new DoctorService();

        createPanel();
    }


    /**
     * Creates JPanel using MigLayout to structure the header, table and buttons.
     */
    private void createPanel() {
        setLayout(new MigLayout("align center, insets 20", "[center]", "[center][center]"));

        JPanel specialityPanel = new JPanel(new MigLayout("wrap, insets 15 45, gapy 10", "fill, 250:280"));
        specialityPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("Edit Speciality");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        specialityPanel.add(title, "span, gapx 70, gapy 0, height 30");


        JLabel specialHeader = new JLabel("Speciality:");
        specialityPanel.add(specialHeader, "gaptop 20");

        docSpeciality = new JTextField();
        specialityPanel.add(docSpeciality,"height 30");

        updateBtn = new JButton("Update Speciality");
        updateBtn.addActionListener(this);
        specialityPanel.add(updateBtn, "gaptop 10, height 30");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(150, 25));

        add(specialityPanel, "cell 0 0, gapbottom 50");
        add(backBtn, "cell 0 1");
    }

    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and updating doctor speciality via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == updateBtn) {
            //Different if statements to check the missing fields that the doctor has not filled out.
            if (docSpeciality.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter a Speciality");
                return;
            }

            int specialChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to set your Speciality to " + docSpeciality.getText(),"Confirm Speciality", JOptionPane.YES_NO_OPTION);
            if (specialChoice == 0) {
                //SQL Query to update doctors speciality to the text entered into the docSpeciality field.
                doctorService.updateDoctorSpecialty(doctor.doctorId(), docSpeciality.getText());
                logService.addLog(doctor.doctorId(), "Registered Speciality");
                JOptionPane.showMessageDialog(this, "Your Registered Speciality has been Updated.");
                cardLayout.show(switchPanel, "doctor");
            }
        }

        //If back button is selected, doctor returns back to DoctorPage (Home Page)
        else if (e.getSource() == backBtn) {
            main.showDoctorPanel(doctor);
        }
    }
}

