import app.service.VisitService;
import app.ui.users.Doctor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnterVisitDetailsTest {
    private final VisitService visitService = new VisitService();

    /**
     * Inserts a new entry into the visit details table, stored prescription as hidden
     * Checks to see if prescription is accurately stored as hidden.
     */
    @Test
    public void checkNewEntryTest() {
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        visitService.insertVisit(2, doctor.doctorId(), "test", "hidden", LocalDateTime.now());

        List<String[]> userVisits = visitService.getVisits(doctor.doctorId());
        assertEquals("hidden", userVisits.getLast()[4]);
    }
}
