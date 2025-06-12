package app.ui.menu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import app.Main;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import app.service.DoctorService;
import app.service.UserService;
import app.service.VisitService;
import app.ui.tables.VisitTable;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

/**
 * Class to allow doctor to view details of patient visits.
 */
public class ViewVisitDetails extends JPanel implements ActionListener {
    private final Main main;
    private final JPanel switchPanel;
    private final CardLayout cardLayout;
    private VisitTable VTable;
    private final Doctor doctor;
    private JButton backBtn, editBtn;
    private final UserService userService;
    private JLabel headerLabel;
    private final VisitService visitService;
    private List<String[]> userVisits;


    public ViewVisitDetails(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        userService = new UserService();
        visitService = new VisitService();

        createPanel();
        loadVisitData();
    }

    /**
     * Creates JPanel using MigLayout to structure the header, table and buttons.
     */
    private void createPanel () {
        setLayout(new MigLayout("fill, insets 20", "[grow, center]", "[][grow][][]"));
        putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 10%);" +
                "[dark]background:lighten(@background, 10%)"
        );

        headerLabel = new JLabel("Visit Details");
        headerLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");
        add(headerLabel, "cell 0 0, wrap");

        //Creates JTable with scroll pane, allowing it to be scrollable with enough rows in the table
        VTable = new VisitTable();
        VTable.setPreferredScrollableViewportSize(new Dimension(1000, 600));
        JScrollPane scrollPane = new JScrollPane(VTable);
        add(scrollPane, "cell 0 1, grow, push, gapy 20");

        editBtn = new JButton("Edit Details");
        editBtn.addActionListener(this);
        editBtn.setFocusable(false);
        editBtn.setPreferredSize(new Dimension(100, 50));
        add(editBtn, "cell 0 2, align right, wrap, gapy 20");

        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(100, 25));
        add(backBtn, "cell 0 3, wrap");
    }

    /**
     * Loads visit details into JTable to be viewed.
     */
    private void loadVisitData() {
        userVisits = visitService.getVisits(doctor.doctorId());

        //Adds data into the column for each row in the JTable
        if (userVisits != null) {
            DefaultTableModel model = VTable.getModel();
            model.setRowCount(0);
            for (String[] visit : userVisits) {
                String[] row = {visit[0], visit[1], visit[2], visit[3], visit[4], visit[5]};
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

        else if (e.getSource() == editBtn) {
            //Allows doctor to edit visit details once a row has been selected
            if (VTable.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Please Select a Row to Edit");
                return;
            }

            //Creates a new window using JDialog for the doctorto update the details of selected row
            JDialog editDetails = new JDialog(main, "Edit Details", true);
            editDetails.setContentPane(new EditVisitDetails(userVisits, VTable.getSelectedRow(), doctor));
            editDetails.pack();
            editDetails.setLocationRelativeTo(main);
            editDetails.setVisible(true);

            //After editDetails Dialog is closed it reloads the data of the table
            loadVisitData();
        }
    }
}
