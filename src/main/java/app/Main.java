package app;

import app.ui.menu.DoctorPage;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.fonts.roboto_mono.FlatRobotoMonoFont;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.UIManager;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import app.ui.auth.Login;
import app.ui.auth.SignUp;
import app.ui.users.Doctor;

/**
 * Class to create entry point of application.
 */
public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel switchPanel;

    public Main() {
        createFrame();
    }

    /**
     * Main frame of application for panels to be displayed on.
     */
    private void createFrame() {
        setTitle("General Practitioner System");
        setSize(new Dimension(1200, 800));
        setLayout(new MigLayout("fill, insets 0", "[grow]", "[grow]"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        switchPanel = new JPanel(cardLayout);
        switchPanel.add(new Login(cardLayout, switchPanel, this),"login");
        switchPanel.add(new SignUp(cardLayout, switchPanel, this), "signup");
        add(switchPanel, "grow");
    }

    /**
     * Used to switch to a new panel.
     * @param panelName a String representing the object of the panel.
     */
    public void showPanel(String panelName) {
        cardLayout.show(switchPanel, panelName);
    }

    /**
     * For Doctors to access their specialised home page.
     * @param doctor record to encapsulate all the details required of the doctor on sign up to identify them.
     */
    public void showDoctorPanel(Doctor doctor) {
        DoctorPage doctorPage = new DoctorPage(cardLayout, switchPanel, doctor,this);
        switchPanel.add(doctorPage, "doctor");
        showPanel("doctor");
    }


    /**
     * Installing Fonts, setting up look and feel library then invoking Main to run it as entry point.
     */
    public static void main(String[] args) {
        FlatRobotoMonoFont.install();
        FlatLaf.registerCustomDefaultsSource("themes");
        UIManager.put("defaultFont", new Font(FlatRobotoMonoFont.FAMILY, Font.PLAIN, 12));
        FlatLightLaf.setup();
        EventQueue.invokeLater(() -> new Main().setVisible(true));
    }
}
