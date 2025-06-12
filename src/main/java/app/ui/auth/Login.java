package app.ui.auth;

import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import app.Main;
import app.service.LogService;
import app.service.UserService;
import app.service.DoctorService;
import app.ui.menu.DoctorPage;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

/**
 * Class to allow users to Login with valid credentials to gain access to the system.
 */
public class Login extends JPanel implements ActionListener {
    private final Main main;
    private DoctorPage home;
    private final UserService userService;
    private final DoctorService doctorService;
    private final LogService logService;
    private final CardLayout cardLayout;
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton,newMember, forgotPassword;;
    private final JPanel switchPanel;

    public Login(CardLayout cardLayout, JPanel switchPanel, Main main) {
        this.userService = new UserService();
        this.doctorService = new DoctorService();
        this.logService = new LogService();
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        createPanel();

        addHoverEffect(newMember, forgotPassword);

    }

    /**
     * Creates the panel for Login components to be added on to.
     */
    private void createPanel() {
        //[center], [center] constraint addresses the row and column of the JPanel
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        //MigLayout uses insets for padding and fill to allow the JPanel to fill the JFram
        JPanel loginPanel = new JPanel(new MigLayout("wrap, insets 35 45", "fill, 250:280"));
        //Uses FlatLaf library to update UI
        loginPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("Log In");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");

        userField = new JTextField();
        userField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");


        passwordField = new JPasswordField();
        passwordField.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");

        //ActionListener allows functionality to be added upon button press
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        //Removes the button background so the Forgot password option appears as a clickable label.
        forgotPassword = new JButton("Forgot password?");
        forgotPassword.addActionListener(this);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setBorderPainted(false);
        forgotPassword.setOpaque(false);
        loginPanel.add(forgotPassword);

        JLabel askMember = new JLabel("Don't have an account?");

        newMember = new JButton("Sign Up");
        newMember.addActionListener(this);
        newMember.setContentAreaFilled(false);
        newMember.setBorderPainted(false);
        newMember.setOpaque(false);

        loginPanel.add(title, "gapx 100");
        loginPanel.add(userField, "gapy 30, height 30");
        loginPanel.add(passwordField, "gapy 10, height 30");
        loginPanel.add(loginButton, "gapy 20, height 30");
        loginPanel.add(forgotPassword, "gapy 10, wrap");
        loginPanel.add(askMember, "gapy 10, split 2");
        loginPanel.add(newMember);

        add(loginPanel);
    }

    /**
     * Clears username and password fields
     */
    private void clearData() {
        userField.setText("");
        passwordField.setText("");
    }


    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and authenticate users.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newMember) {
            cardLayout.show(switchPanel, "signup");
        }

        else if (e.getSource() == loginButton) {
            String user = userField.getText();
            String password = String.valueOf(passwordField.getPassword());

            if (userService.checkUserCredentials(user, password)) {
                //Creates a record of the doctor if the user with the entered user credentials has a role of doctor
                if (userService.getRole(user).equalsIgnoreCase("doctor")) {
                    Doctor doctor = new Doctor(userService.getUserId(user), userService.getUserFirstName(user),
                            userService.getUserLastName(user),
                            doctorService.getUserSpeciality(userService.getUserId(user)));
                    main.showDoctorPanel(doctor);
                    logService.addLog(doctor.doctorId(), "Logged In");
                    clearData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Email or Password");
            }
        }
    }


    // Add hover effect to components
    private void addHoverEffect(JButton signupBtn, JButton forgot) {
        // Hover effect for the Sign Up button
        signupBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                signupBtn.setForeground(new Color(0, 123, 255)); // Change to blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signupBtn.setForeground(UIManager.getColor("Label.foreground")); // Reset to default color
            }

        });

        forgot.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent  e) {
                forgot.setForeground(new Color(0, 123, 255)); // Change to blue on hover
            }

            @Override
            public void mouseExited(MouseEvent  e) {
                forgot.setForeground(UIManager.getColor("Label.foreground")); // Reset to default color
            }

        });


    }
}
