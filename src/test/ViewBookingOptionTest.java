import app.Main;
import app.service.BookingService;
import java.util.List;
import app.ui.users.Doctor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ViewBookingOptionTest {
    private final BookingService bookingService = new BookingService();

    /**
     * Retrieving booking records in month of April
     * Checking whether a month part of those records is the same as the month specified to be looking for.
     */
    @Test
    public void testCorrectDate() {
        Doctor doctor = new Doctor(1, "John", "Doe", "");
        List<String[]> bookingRecords = bookingService.getDoctorBookings(doctor.doctorId(), 4, 2025);
        System.out.println(bookingRecords.getFirst()[3]);
        String appointDate = bookingRecords.getFirst()[3].split(" ")[0];
        String appointMonth = appointDate.split("-")[1];

        assertEquals(3, Integer.parseInt(appointMonth) - 1);

    }
}

