import app.service.VisitService;
import app.ui.users.Doctor;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EditVisitDetailsTest {
    private final VisitService visitService = new VisitService();

    /**
     * Verifies whether the fields were updated by checking if the records are the same
     */
    @Test
    public void checkVisitDetailChangeTest() {
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        List<String[]> userVisits = visitService.getVisits(doctor.doctorId());
        String[] testRecord = userVisits.getFirst();
        visitService.updateVisitDetails(1, "test", "test", LocalDateTime.now());

        List<String[]> newUserVisits = visitService.getVisits(doctor.doctorId());
        assertNotEquals(testRecord, newUserVisits.getFirst());
    }
}
