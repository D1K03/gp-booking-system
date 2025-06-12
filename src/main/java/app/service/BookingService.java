package app.service;

import app.database.BookingDAO;
import java.sql.SQLException;
import java.util.List;

public class BookingService {
    private BookingDAO bookingData = new BookingDAO();


    /**
     * Calls the getDoctorBookings method from the Booking Data Access Object class (BookingDAO) to get booking records in specified timeframe.
     * Catches SQL Exception if query fails.
     * @return a List of String arrays representing booking records
     */
    public List<String[]> getDoctorBookings(int doctorId, int month, int year) {
        try {
            return bookingData.getDoctorBookings(doctorId, month, year);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
