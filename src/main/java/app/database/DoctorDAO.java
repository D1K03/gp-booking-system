package app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    /**
     * Retrieves doctors Id along with their full name.
     * @return String array of all doctors
     * @throws SQLException if SQL query fails due to database connectivity or invalid query.
     */
    public String[] getAllDoctors() throws SQLException {
        String doctorQuery = "SELECT d.doctor_id, " + "CONCAT(u.first_name, ' ', u.last_name) AS full_name " + "FROM doctors d " +
                "JOIN users u ON d.user_id = u.user_id";
        List<String> doctorRecords = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getAllDoctorsStatement = conn.prepareStatement(doctorQuery);
             ResultSet rs = getAllDoctorsStatement.executeQuery()) {

            while (rs.next()) {
                int userId = rs.getInt("doctor_id");
                String fullName = rs.getString("full_name");
                String record = userId + " " + fullName;
                doctorRecords.add(record);
            }
        }
        return doctorRecords.toArray(new String[0]);
    }

    /**
     * Gets speciality of user, uniquely identified by UserId, as it is a foreign key in doctors table.
     * @return Speciality
     */
    public String getUserSpeciality(int userId) throws SQLException {
        String query = "SELECT speciality FROM doctors WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getSpecialityStatement = conn.prepareStatement(query)) {
            getSpecialityStatement.setInt(1, userId);
            ResultSet rs = getSpecialityStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("speciality");
            }
        }
        return null;
    }

    /**
     * Updates the speciality for a doctor identified by their doctor Id.
     * @param doctorId the unique id of the doctor
     * @param speciality the new specialty of doctor
     */
    public void updateDoctorSpecialty(int doctorId, String speciality) throws SQLException {
        String query = "UPDATE doctors SET speciality = ? WHERE doctor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateSpecialStatement = conn.prepareStatement(query)) {

            updateSpecialStatement.setString(1, speciality);
            updateSpecialStatement.setInt(2, doctorId);

            int rowsUpdated = updateSpecialStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No doctor found with doctorid: " + doctorId);
            }
        }
    }
}

