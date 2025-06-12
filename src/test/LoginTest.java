import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import app.service.UserService;

public class LoginTest {

    /**
     * Tests authentication with incorrect username.
     */
    @Test
    public void testIncorrectUsername() {
        UserService userService = new UserService();
        assertFalse(userService.checkUserCredentials("JohnDoe", "1234567"));
    }



    /**
     * Tests authentication with incorrect password.
     */
    @Test
    public void testIncorrectPassword() {
        UserService userService = new UserService();
        assertFalse(userService.checkUserCredentials("JohnDoe123", "543210"));
    }

    /**
     * Tests authentication with incorrect username and password.
     */
    @Test
    public void testBothIncorrectFields() {
        UserService userService = new UserService();
        assertFalse(userService.checkUserCredentials("JohnDoe", "543210"));
    }

    /**
     * Tests authentication with correct username and password.
     */
    @Test
    public void testValidCredentials() {
        UserService userService = new UserService();
        assertTrue(userService.checkUserCredentials("JohnDoe123", "1234567"));
    }
}
