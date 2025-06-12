import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import app.service.UserService;
import app.ui.auth.SignUp;
import org.junit.jupiter.api.Test;

public class SignUpTest {

    /**
     * Tests input into database with some valid inputs
     */
    @Test
    public void testEmptyFields() {
        UserService userService = new UserService();

        String user = "";
        String firstName = "";
        String lastName = "";
        String phone = "";
        String email = "";
        String pass  = "";
        String role = "";
        java.sql.Date sqlDate = null;
        String address = "";
        String postal = "";
        String city = "";
        Timestamp regDate = Timestamp.valueOf(LocalDateTime.now());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user, firstName, lastName, email, sqlDate,
                    phone, address, postal, city, role, pass, regDate);
        });
    }

    /**
     * Tests input into database with some valid inputs.
     */
    @Test
    public void testSomeEmptyFields() {
        UserService userService = new UserService();

        String user = "jk2015";
        String firstName = "";
        String lastName = "Kennedy";
        String phone = "";
        String email = "jk2015@gmail.com";
        String pass  = "";
        String role = "";
        java.sql.Date sqlDate = null;
        String address = "";
        String postal = "";
        String city = "London";
        Timestamp regDate = Timestamp.valueOf(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user, firstName, lastName, email, sqlDate,
                    phone, address, postal, city, role, pass, regDate);
        });
    }

    /**
     * Tests input into the database with all valid inputs.
     */
    @Test
    public void testValidFields() {
        UserService userService = new UserService();

        String user = "JaneDoe123";
        String firstName = "Jane";
        String lastName = "Doe";
        String phone = "9876543210";
        String email = "JaneDoe123@gmail.com";
        String pass  = "1234567";
        String role = "patient";
        String stringDate = "1992-02-08";
        LocalDate date = LocalDate.parse(stringDate);
        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
        String address = "21 Downing Street";
        String postal = "SW14 2AD";
        String city = "London";
        Timestamp regDate = Timestamp.valueOf(LocalDateTime.now());

        assertNotEquals(-1, userService.addUser(user, firstName, lastName, email, sqlDate,
                    phone, address, postal, city, role, pass, regDate));
    }
}
