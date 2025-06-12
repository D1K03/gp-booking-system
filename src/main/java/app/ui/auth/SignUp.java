package app.ui.auth;

import app.Main;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import com.github.lgooddatepicker.components.DatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import app.service.UserService;
import javax.swing.*;

/**
 * Class to users to Sign Up with their credentials to gain access to the system.
 */
public class SignUp extends JPanel implements ActionListener {
    private Main main;
    private final UserService userService;
    private final CardLayout cardLayout;
    private JTextField username, firstName, lastName, emailAddress, homeAddress, postCode, cityLocation, phoneNumber;
    private DatePicker dateOfBirth;
    private JPasswordField password, confirmPassword;
    private JButton signupButton,oldMember;
    private final JPanel switchPanel;
    private JComboBox roleMenu;

    public SignUp(CardLayout cardLayout, JPanel switchPanel, Main main) {
        this.userService = new UserService();
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;

        createPanel();
        addHoverEffect(oldMember);
    }

    /**
     * Creates panel for Sign Up components to be added on to.
     */
    private void createPanel() {
        //[center], [center] constraint addresses the row and column of the JPanel
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));
        //MigLayout uses insets for padding and fill to allow the JPanel to fill the JFrame
        JPanel signupPanel = new JPanel(new MigLayout("wrap, insets 35 45", "fill, 250:280"));
        signupPanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );

        JLabel title = new JLabel("Sign Up");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 24");

        JLabel personalHeader = new JLabel("Personal Information");
        personalHeader.putClientProperty(FlatClientProperties.STYLE, "font:bold");


        firstName = new JTextField();
        firstName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "First Name");

        lastName = new JTextField();
        lastName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Last Name");

        //External dependency allowing users to select their date of birth efficiently
        dateOfBirth = new DatePicker();

        JLabel contactHeader = new JLabel("Contact Information");
        contactHeader.putClientProperty(FlatClientProperties.STYLE, "font:bold");


        phoneNumber = new JTextField();
        phoneNumber.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Phone Number");

        emailAddress = new JTextField();
        emailAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Email Address");

        homeAddress = new JTextField();
        homeAddress.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Address Line");

        postCode = new JTextField();
        postCode.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Zip/Postal Code");

        cityLocation = new JTextField();
        cityLocation.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "City");

        JLabel createHeader = new JLabel("Create your Login");
        createHeader.putClientProperty(FlatClientProperties.STYLE, "font:bold");


        username = new JTextField();
        username.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");

        password = new JPasswordField();
        password.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");

        confirmPassword = new JPasswordField();
        confirmPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        confirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirm Password");

        //ComboBox used to store the different roles the user can select
        String[] roles = {"Select Role", "Patient", "Receptionist", "Doctor", "Admin"};
        roleMenu = new JComboBox<>(roles);
        roleMenu.setSelectedIndex(0);

        //ActionListener allows functionality to be added upon button press
        signupButton = new JButton("Sign Up");
        signupButton.addActionListener(this);

        JLabel askMember = new JLabel("Already have an account?");

        oldMember = new JButton("Login");
        oldMember.addActionListener(this);
        oldMember.setContentAreaFilled(false);
        oldMember.setBorderPainted(false);
        oldMember.setOpaque(false);

        signupPanel.add(title, "gapx 85");
        signupPanel.add(personalHeader, "gapy 30, height 30");
        signupPanel.add(firstName, "height 30, split 2");
        signupPanel.add(lastName, "height 30");
        signupPanel.add(emailAddress, "gapy 10, height 30");
        signupPanel.add(dateOfBirth, "gapy 10, height 30");
        signupPanel.add(phoneNumber, "gapy 10, height 30");
        signupPanel.add(contactHeader, "gapy 10, height 30");
        signupPanel.add(homeAddress, "gapy 0, height 30");
        signupPanel.add(postCode, "gapy 10, height 30");
        signupPanel.add(cityLocation, "gapy 10, height 30");
        signupPanel.add(createHeader, "gapy 10, height 30");
        signupPanel.add(username, "gapy 0, height 30");
        signupPanel.add(password, "gapy 10, height 30");
        signupPanel.add(confirmPassword, "gapy 10, height 30");
        signupPanel.add(roleMenu, "gapy 10, height 30");
        signupPanel.add(signupButton, "gapy 20, height 30");
        signupPanel.add(askMember, "gapy 10, split 2");
        signupPanel.add(oldMember);

        add(signupPanel);
    }

    /**
     * Checks whether any text has been written in text fields for registration
     * @param text where description goes
     * @return boolean indicating whether at least one of the fields is missing text
     */
    private boolean checkEmpty(String text) {
        return text.trim().isEmpty();
    }

    /**
     * Used to check whether a button is pressed, if so it will do the if condition, e.g. switching panels and adding user entries via SQL query.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Switches back to Login page if button is pressed
        if (e.getSource() == oldMember) {
            cardLayout.show(switchPanel, "login");
        }

        //If Sign Up button is pressed and password and confirm password match, it processes the if statement
        else if (e.getSource() == signupButton && String.valueOf(password.getPassword()).equals(String.valueOf(confirmPassword.getPassword())) && roleMenu.getSelectedIndex() != 0) {
            String email = emailAddress.getText();
            if (userService.checkEmail(email)) {
                JOptionPane.showMessageDialog(this,"User with Email already Registered.");
            } else {
                String forename = firstName.getText();
                String surname = lastName.getText();
                String phone = phoneNumber.getText();
                String role = (String) roleMenu.getSelectedItem();
                String address = homeAddress.getText();
                String postal = postCode.getText();
                String city = cityLocation.getText();
                LocalDate dob = dateOfBirth.getDate();
                Timestamp regDate = Timestamp.valueOf(LocalDateTime.now());
                String user = username.getText();
                String pass = String.valueOf(password.getPassword());

                JTextField[] fieldData = new JTextField[] {firstName, lastName, phoneNumber, homeAddress, postCode, cityLocation, username, emailAddress};
                String[] queryData = new String[] {user, forename, surname, email, phone, role, address, postal, city, pass};

                //Verifies whether valid data has been passed into the fields
                boolean isValid = true;
                for (String data : queryData) {
                    if (checkEmpty(data)) {isValid = false;}
                }

                if (dob == null) {isValid = false;}

                if (isValid) {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(dob);

                    //SQL Query to add new user credentials into the database
                    userService.addUser(user, forename, surname, email, sqlDate, phone, address, postal, city, role, pass, regDate);
                    JOptionPane.showMessageDialog(this, "Registration Successful");

                    //Clears all fields once user credentials have been added into the database
                    for (JTextField data : fieldData) {data.setText("");}
                    roleMenu.setSelectedIndex(0);
                    dateOfBirth.setText("");
                    password.setText("");
                    confirmPassword.setText("");

                    cardLayout.show(switchPanel, "login");
                    } else {
                    //Error message if all required fields have not been filled
                    JOptionPane.showMessageDialog(this, "Please fill in all the fields");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Password or Missing Roles");
        }

    }

    private void addHoverEffect(JButton loginBtn) {
        // Hover effect for the Sign Up button
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginBtn.setForeground(new Color(0, 123, 255)); // Change to blue on hover
            }

            @Override
            public void mouseExited(MouseEvent  e) {
                loginBtn.setForeground(UIManager.getColor("Label.foreground")); // Reset to default color
            }

        });

    }

}
