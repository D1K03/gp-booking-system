import app.service.DoctorService;
import app.ui.users.Doctor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EditSpecialityTest {
    private final DoctorService doctorService = new DoctorService();

    /**
     * Checks whether updateDoctorSpecialty actually changes the Speciality stored in the Doctor table
     */
    @Test
    public void testUpdateSpeciality() {
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        doctorService.updateDoctorSpecialty(doctor.doctorId(), "Test");
        assertEquals("Test", doctorService.getUserSpeciality(1));
    }
}