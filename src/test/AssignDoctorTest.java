import org.junit.jupiter.api.Test;
import app.service.PatientService;
import app.ui.users.Doctor;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AssignDoctorTest {
    private final PatientService patientService = new PatientService();

    /**
     * Checks if the patient has been moved out of the previous doctors my patients list
     */
    @Test
    public void testAssignNewDoctorAction() {
        Doctor doctor = new Doctor(2, "Jane", "Doe", "");
        String[] initialPatients = patientService.getMyPatients(2);
        System.out.println(Arrays.toString(initialPatients));

        patientService.updatePatientDoctor(1,1);
        assertNotEquals(initialPatients.length, patientService.getMyPatients(2).length);

    }
}


