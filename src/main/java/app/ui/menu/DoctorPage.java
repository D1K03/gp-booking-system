package app.ui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import app.Main;
import javax.swing.*;

import app.service.LogService;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

/**
 * Class used as homepage for doctors to navigate between different panels in the system.
 */
public class DoctorPage extends JPanel implements ActionListener {
    private final Main main;
    private final JPanel switchPanel;
    private final CardLayout cardLayout;
    private final LogService logService;
    private JButton patientBtn, bookingBtn, enterVisitBtn, viewVisitBtn, sendMessageBtn, assignBtn, specialBtn, logBtn;
    private final JButton[] homeBtns = new JButton[] {patientBtn, bookingBtn, enterVisitBtn, viewVisitBtn, sendMessageBtn, assignBtn, specialBtn, logBtn};
    private final Doctor doctor;


    public DoctorPage(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        logService = new LogService();

        createPanel();
    }

    /**
     * Creates JPanel using MigLayout to structure the header, table and buttons.
     */
    private void createPanel () {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[][center]"));
        JPanel homePanel = new JPanel(new GridLayout(2, 4, 4, 4));

        JLabel welcomeTitle = new JLabel("Welcome " + doctor.firstName() + " " + doctor.lastName() + "!");
        welcomeTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");

        //For loop at initialise each button, since they are all the same size there was no need to do it separately.
        String[] btnLabels = new String[] {"View Patients", "View Bookings", "Enter Visit Details", "View Visit Details", "Send Message", "Assign Doctor", "Edit Speciality", "Log Out"};
        for (int i = 0; i < homeBtns.length; i++) {
            homeBtns[i] = new JButton(btnLabels[i]);
            homeBtns[i].setActionCommand(btnLabels[i]);
            homeBtns[i].addActionListener(this);
            homeBtns[i].setPreferredSize(new Dimension(200, 100));
            homeBtns[i].setFocusable(false);
            homePanel.add(homeBtns[i]);
        }

        //Assigning the initialised buttons to a button.
        patientBtn = homeBtns[0];
        bookingBtn = homeBtns[1];
        enterVisitBtn = homeBtns[2];
        viewVisitBtn = homeBtns[3];
        sendMessageBtn = homeBtns[4];
        assignBtn = homeBtns[5];
        specialBtn = homeBtns[6];
        logBtn = homeBtns[7];

        add(welcomeTitle, "cell 0 0, wrap");
        add(homePanel, "cell 0 1");
    }

    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();

        //Switch cases to handle each button press on the home page to open a new panel.
        switch (action) {
            case "Log Out" -> {
                cardLayout.show(switchPanel, "login");
                logService.addLog(doctor.doctorId(), "Logged Out");
            }
            case "View Patients" -> {
                switchPanel.add(new ViewPatients(cardLayout, switchPanel, doctor, main), "patients");
                logService.addLog(doctor.doctorId(), "Viewed Patients");
                cardLayout.show(switchPanel, "patients");
            }
            case "View Bookings" -> {
                switchPanel.add(new ViewBookingOption(cardLayout, switchPanel, doctor, main), "bookings");
                logService.addLog(doctor.doctorId(), "Viewed Bookings");
                cardLayout.show(switchPanel, "bookings");
            }
            case "Assign Doctor" -> {
                switchPanel.add(new AssignDoctor(cardLayout, switchPanel, doctor, main), "assign");
                cardLayout.show(switchPanel, "assign");
            }
            case "Send Message" -> {
                switchPanel.add(new SendMessage(cardLayout, switchPanel, doctor, main), "message");
                cardLayout.show(switchPanel, "message");
            }
            case "Enter Visit Details" -> {
                switchPanel.add(new EnterVisitDetails(cardLayout, switchPanel, doctor, main), "enterdetails");
                cardLayout.show(switchPanel, "enterdetails");
            }
            case "View Visit Details" -> {
                switchPanel.add(new ViewVisitDetails(cardLayout, switchPanel, doctor, main), "viewdetails");
                logService.addLog(doctor.doctorId(), "Viewed Visit Details");
                cardLayout.show(switchPanel, "viewdetails");
            }
            case "Edit Speciality" -> {
                switchPanel.add (new EditSpeciality(cardLayout, switchPanel, doctor, main), "speciality");
                cardLayout.show(switchPanel, "speciality");
            }
        }
    }
}
