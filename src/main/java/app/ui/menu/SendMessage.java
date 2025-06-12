package app.ui.menu;

import app.Main;
import app.service.LogService;
import app.service.UserService;
import app.ui.users.Doctor;
import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import app.service.MessageService;
import  javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class to allow Doctors to send messages to admins or receptionists.
 */
public class SendMessage extends JPanel implements ActionListener {
    private JComboBox<String> roleDropdown;
    private JComboBox<String> usersDropdown;
    private final Main main;
    private final Doctor doctor;
    private final JPanel switchPanel;
    private final UserService userService;
    private final MessageService messageService;
    private final LogService logService;
    private JTextArea messageTextArea;
    private final CardLayout cardLayout;
    private JButton sendBtn, backBtn;


    public SendMessage(CardLayout cardLayout, JPanel switchPanel, Doctor doctor, Main main) {
        this.cardLayout = cardLayout;
        this.switchPanel = switchPanel;
        this.main = main;
        this.doctor = doctor;
        logService = new LogService();
        messageService = new MessageService();
        userService = new UserService();

        createPanel();
    }

    /**
     * MigLayout for the structure of the panel for a more flexible way to adds components like labels, dropdowns, buttons, etc .
     */
    private void createPanel() {
        setLayout(new MigLayout("align center, insets 20", "[center]", "[center][center]"));

        // A container to hold all the message-related components
        JPanel messagePanel = new JPanel(new MigLayout("wrap, insets 15 45", "fill, 250:280"));
        // Styling to make the panel look more modern
        messagePanel.putClientProperty(FlatClientProperties.STYLE, "arc:20;" +
                "[light]background:darken(@background, 5%);" +
                "[dark]background:lighten(@background, 5%)"
        );
        // Title/heading for this page
        JLabel title = new JLabel("Message Receptionist/Admin");
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold 16");
        messagePanel.add(title, "gapx 15, gapbottom 10");


        // Different roles available that the user can send a message to, which will be used to retrieve the users.
        String[] roles = new String[] {"Select Role", "Receptionist", "Admin"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.addActionListener(this);
        messagePanel.add(roleDropdown, "height 30, gaptop 10");


        String[] userPlaceholder = new String[] {"Select User"};
        usersDropdown = new JComboBox<>(userPlaceholder);
        messagePanel.add(usersDropdown, "height 30, gapy 10");

        JLabel messageHeader = new JLabel("Message:");
        messagePanel.add(messageHeader, "gaptop 10");

        messageTextArea = new JTextArea(5, 20);
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messagePanel.add(new JScrollPane(messageTextArea), "growx, gapbottom 10");

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(this);
        messagePanel.add(sendBtn, "span, align center, height 30");

        //Back button to return to doctor's main panel.
        backBtn = new JButton("Back");
        backBtn.addActionListener(this);
        backBtn.setFocusable(false);
        backBtn.setPreferredSize(new Dimension(150, 25));

        add(messagePanel, "cell 0 0, gapbottom 50");
        add(backBtn, "cell 0 1");
    }

    /**
     * This method handles what happens when buttons or dropdowns are clicked/used.
     * @param e the event to be processed (e.g. clicking "Send")
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Gets all admins or receptionists depending on the role selected from the ComboBox.
        if (e.getSource() == roleDropdown && roleDropdown.getSelectedIndex() != 0) {
            int selectedIndex = roleDropdown.getSelectedIndex();
            System.out.println(selectedIndex);
            String selectedRole = ((String) roleDropdown.getSelectedItem()).toLowerCase();
            String[] availableUsers = userService.getUsersByRole(selectedRole);

            //Adds a Select User option as a placeholder text along with the available users to send a message.
            String[] users = new String[availableUsers.length + 1];
            users[0] = "Select User";
            for (int i = 1; i < users.length; i++) {
                users[i] = availableUsers[i-1];
            }
            usersDropdown.setModel(new DefaultComboBoxModel<>(users));

        }

        // Sender, Recipient and Message stored in SQL Table via query after doctor sends message.
        else if (e.getSource() == sendBtn) {
            if (usersDropdown.getSelectedIndex() == 0 & roleDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please Fill in the Fields");
                return;
            }
            else if (roleDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please select a Role");
                return;
            }
            //Make sure a user is selected
            else if (usersDropdown.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Please Select a User");
                return;
            }

            String selectedUser = (String) usersDropdown.getSelectedItem();

            int receiverId = Integer.parseInt(selectedUser.split(" ")[0]);
            String receiverName = selectedUser.split(" ")[1] + selectedUser.split(" ")[2];
            String messageContent = messageTextArea.getText().trim();

            if (messageContent.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Message Cannot be Empty");
                return;
            }

            int messageChoice = JOptionPane.showConfirmDialog(this, "Are you sure you want to send a message to " + receiverName, "Confirm Message", JOptionPane.YES_NO_OPTION);
            if (messageChoice == 0) {
                //SQL Query that adds the Id of the doctor, the Id of the receiver and the message information into the database along with the timestamp.
                messageService.sendMessage(doctor.doctorId(), receiverId, messageContent);
                logService.addLog(doctor.doctorId(), "Sent a Message");
                JOptionPane.showMessageDialog(null, "Message Sent Successfully");
                messageTextArea.setText("");
                main.showDoctorPanel(doctor);
            }
        }
        // If back button is clicked, go back to doctor's main page
        else if (e.getSource() == backBtn) {
            main.showDoctorPanel(doctor);
        }
    }
}


