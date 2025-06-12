package app.ui.menu;

import app.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import app.service.LogService;
import app.service.VisitService;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import com.github.lgooddatepicker.components.DateTimePicker;
import net.miginfocom.swing.MigLayout;

/**
 * Class to allow doctors to track user visit history by entering details of their visit and prescription.
 */
public class EditVisitDetails extends JPanel implements ActionListener {
    private final VisitService visitService;
    private final int currentRow;
    private final Doctor doctor;
    private final LogService logService;
    private final List<String[]> userVisits;
    private DateTimePicker dateArrival;
    private JTextArea notesTextArea;
    private JTextArea messageTextArea;
    private JButton enterButton, closeBtn;


    public EditVisitDetails(List<String[]> userVisits, int currentRow, Doctor doctor) {
        this.currentRow = currentRow;
        this.userVisits = userVisits;
        this.doctor = doctor;
        logService = new LogService();
        visitService = new VisitService();
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

        JLabel title = new JLabel("Edit Visit Details");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        enterPanel.add(title,"gapx 50, gapbottom 20");

        JLabel visitDate = new JLabel("Visit Date:");
        enterPanel.add(visitDate);

        //Converts the string of the DateTime back to LocalDateTime reference type.
        dateArrival = new DateTimePicker();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDate = LocalDateTime.parse(userVisits.get(currentRow)[5], formatter);
        dateArrival.setDateTimeStrict(localDate);
        enterPanel.add(dateArrival, "height 30, gapbottom 10");

        JLabel messageTitle = new JLabel("Prescription:");
        enterPanel.add(messageTitle);

        //Text area used for the doctor to enter any relevant prescription details.
        messageTextArea = new JTextArea(5, 20);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setText(userVisits.get(currentRow)[4]);
        enterPanel.add(new JScrollPane(messageTextArea), "growx, gapbottom 10");

        JLabel notesTitle = new JLabel("Additional Notes:");
        enterPanel.add(notesTitle);

        notesTextArea = new JTextArea(3, 20);
        notesTextArea.setLineWrap(true);
        notesTextArea.setWrapStyleWord(true);
        notesTextArea.setText(userVisits.get(currentRow)[3]);
        enterPanel.add(new JScrollPane(notesTextArea), "growx, gapbottom 15");

        enterButton = new JButton("Update Details");
        enterButton.addActionListener(this);
        enterPanel.add(enterButton, "span, align center, height 30");

        closeBtn = new JButton("Close");
        closeBtn.addActionListener(this);
        closeBtn.setFocusable(false);
        closeBtn.setPreferredSize(new Dimension(150, 25));

        add(enterPanel, "cell 0 0, gapbottom 50");
        add(closeBtn, "cell 0 1");
    }


    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and adding new visit entries via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Represents the window for the EditVisitDetails panel which is opened as a JDialog
        Window window = SwingUtilities.getWindowAncestor(this);

        if (e.getSource() == enterButton) {
            //Checks whether date picked is a valid date.
            LocalDateTime newDateTime = dateArrival.getDateTimeStrict();
            if (newDateTime == null) {
                JOptionPane.showMessageDialog(this,"Please Enter a Valid Visit Date");
                return;
            }

            int updateChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Update Visit Details and Prescriptions for " + userVisits.get(currentRow)[2], "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (updateChoice == 0) {
                //Runs SQL Query to update the notes, prescription and visit date after confirmation then closes the current window.
                visitService.updateVisitDetails(Integer.parseInt(userVisits.get(currentRow)[0]), notesTextArea.getText(), messageTextArea.getText(), newDateTime);
                logService.addLog(doctor.doctorId(), "Edited Visit Details");
                JOptionPane.showMessageDialog(this,"Visit Details and Prescriptions for " + userVisits.get(currentRow)[2] + " have been updated.");
                if (window instanceof JDialog) {
                    ((JDialog) window).dispose();
                }
            }
        }

        //If user closes the EditVisitDetails panel it disposes of the current window.
        else if (e.getSource() == closeBtn) {
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        }
    }
}

