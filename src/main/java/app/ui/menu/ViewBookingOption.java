package app.ui.menu;

import app.service.LogService;
import com.toedter.calendar.JYearChooser;
import com.toedter.calendar.JMonthChooser;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import app.ui.users.Doctor;
import app.Main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to allow Doctors to choose their booking options
 */
public class ViewBookingOption extends JPanel implements ActionListener {
    private final Main main;
    private final Doctor doctor;
    private final JPanel switchPanel;
    private final CardLayout cardLayout;
    private final LogService logService;
    private JMonthChooser monthChoice;
    private JYearChooser yearChoice;
    public JButton bookingBtn, backBtn;


    public ViewBookingOption(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
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
    private void createPanel() {
        setLayout(new MigLayout("align center, insets 20", "[center]", "[center][center]"));

        JPanel bookingPanel = new JPanel(new MigLayout("wrap, insets 15 45, gapy 10", "250:280"));
        bookingPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("View Bookings");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        bookingPanel.add(title, "span, gapx 75, gapy 0, height 30");


        JLabel monthHeader = new JLabel("Booking Month:");
        bookingPanel.add(monthHeader, "gaptop 20");

        //Custom component for choosing a year
        monthChoice = new JMonthChooser();
        bookingPanel.add(monthChoice, "gapbottom 15");

        //Custom component for choosing a year
        JLabel yearHeader = new JLabel("Booking Year:");
        bookingPanel.add(yearHeader);

        yearChoice = new JYearChooser();
        bookingPanel.add(yearChoice, "height 30, width 135");

        bookingBtn = new JButton("Check Bookings");
        bookingBtn.addActionListener(this);
        bookingPanel.add(bookingBtn, "align center, gaptop 25, height 30, width 280");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(150, 25));

        add(bookingPanel, "cell 0 0, gapbottom 50");
        add(backBtn, "cell 0 1");
    }

    private void showBookings(int month, int year) {
        ViewBooking viewBooking = new ViewBooking(cardLayout, switchPanel, doctor, month, year, main);
        switchPanel.add(viewBooking, "bookingdata");
        main.showPanel("bookingdata");
    }


    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and checking for bookings via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bookingBtn) {
            showBookings(monthChoice.getMonth(), yearChoice.getYear());
            logService.addLog(doctor.doctorId(), "Viewed Bookings");
        }

        //If back button is selected, doctor returns back to DoctorPage (Home Page)
        else if (e.getSource() == backBtn) {
            main.showDoctorPanel(doctor);
        }
    }
}

