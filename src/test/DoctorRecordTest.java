import app.service.UserService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorRecordTest {
    private final UserService userService = new UserService();

    /**
     * Checks expected first name matches name stored in user table
     */
    @Test
    public void testCorrectFirstName() {
        assertEquals("John", userService.getUserFirstName("JohnDoe123"));
    }

    /**
     * Checks expected last name matches name stored in user table
     */
    @Test
    public void testCorrectLastName() {
        assertEquals("Doe", userService.getUserLastName("JohnDoe123"));
    }

    /**
     * Checks expected Id name matches name stored in user table
     */
    @Test
    public void testCorrectId() {
        assertEquals(1, userService.getUserId("JohnDoe123"));
    }
}

