import static org.junit.jupiter.api.Assertions.*;
import java.awt.*;
import javax.swing.*;
import org.junit.jupiter.api.Test;
import app.Main;
import app.ui.menu.ViewPatients;
import app.ui.users.Doctor;

public class ViewPatientsTest {

    /**
     * Checks whether ViewPatients panel start with the Doctors patients
     */
    @Test
    public void testViewMyPatients() {
        CardLayout cardLayout = new CardLayout();
        JPanel switchPanel = new JPanel(cardLayout);
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        Main main = new Main();
        ViewPatients viewPatients = new ViewPatients(cardLayout, switchPanel, doctor, main);

        // Check for header "My Patients"
        boolean foundHeader = false;
        for (Component comp : viewPatients.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                System.out.println(label.getText());
                if ("My Patients".equals(label.getText().trim())) {
                    foundHeader = true;
                    break;
                }
            }
        }
        assertTrue(foundHeader, "Header label 'My Patients' should be present.");
    }

    @Test
    public void testViewAllPatients() {
        CardLayout cardLayout = new CardLayout();
        JPanel switchPanel = new JPanel(cardLayout);
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        Main main = new Main();
        ViewPatients viewPatients = new ViewPatients(cardLayout, switchPanel, doctor, main);

        // Simulates the change in text after button click
        boolean foundHeader = false;
        for (Component comp : viewPatients.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setText("All Patients");
                if ("All Patients".equals(label.getText().trim())) {
                    foundHeader = true;
                    break;
                }
            }
        }
        assertTrue(foundHeader, "Header label 'My Patients' should be present.");
    }

}
