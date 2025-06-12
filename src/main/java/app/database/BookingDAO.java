package app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    /** Gets booking records for a particular doctor within the specified month and year.
     * @param doctorId the id of the doctor
     * @param month the month
     * @param year the year
     * @return a List of String arrays where each array represents a booking record
     * @throws SQLException if SQL query fails due to database connectivity or invalid query.
     */
    public List<String[]> getDoctorBookings(int doctorId, int month, int year) throws SQLException {
        List<String[]> bookings = new ArrayList<>();
        String query = "SELECT booking_id, doctor_id, patient_id, appointment_date, status, booking_date " +
                "FROM bookings " + "WHERE doctor_id = ? AND MONTH(appointment_date) = ? AND YEAR(appointment_date) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getBookingStatement = conn.prepareStatement(query)) {

            getBookingStatement.setInt(1, doctorId);
            getBookingStatement.setInt(2, month);
            getBookingStatement.setInt(3, year);

            try (ResultSet rs = getBookingStatement.executeQuery()) {
                while (rs.next()) {
                    String[] booking = new String[6];
                    booking[0] = rs.getString("booking_id");
                    booking[1] = rs.getString("doctor_id");
                    booking[2] = rs.getString("patient_id");
                    booking[3] = rs.getString("appointment_date");
                    booking[4] = rs.getString("status");
                    booking[5] = rs.getString("booking_date");
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }
}
