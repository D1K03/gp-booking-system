package app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisitDAO {


    /**
     * Inserts a new visit record into the database.
     * @param patientId the unique Id of the patient for the visit.
     * @param doctorId the unique Id of the doctor who conducted the visit.
     * @param notes the doctor's notes associated with the visit.
     * @param prescriptions any prescriptions given during the visit.
     * @param visitDate the date and time when the visit occurred.
     * @throws SQLException if SQL query fails due to database connectivity or invalid query.
     */
    public void insertVisit(int patientId, int doctorId, String notes, String prescriptions, LocalDateTime visitDate) throws SQLException {
        String sql = "INSERT INTO visits (patient_id, doctor_id, notes, prescriptions, visit_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertVisitStatement = conn.prepareStatement(sql)) {
            insertVisitStatement.setInt(1, patientId);
            insertVisitStatement.setInt(2, doctorId);
            insertVisitStatement.setString(3, notes);
            insertVisitStatement.setString(4, prescriptions);
            insertVisitStatement.setObject(5, visitDate);

            int affectedRows = insertVisitStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting visit failed, no rows affected.");
            }
        }
    }


    /**
     * Retrieves all visit records and the patients name associated with a specific doctor, patients name is retrieved by
     * joining the patients table to the visits table then joining that to the users table.
     *
     * @param doctorId the unique identifier of the doctor whose visit records are to be retrieved.
     * @return a list of string arrays, where each array represents a visit record while also storing the first name
     * and last name as full name.
     */
    public List<String[]> getVisits(int doctorId) throws SQLException {
        String visitsQuery = "SELECT v.visit_id, v.patient_id, " + "CONCAT(u.first_name, ' ', u.last_name) AS full_name, " +
                "v.notes, v.prescriptions, v.visit_date " + "FROM visits v " + "JOIN patients p ON v.patient_id = p.patient_id " +
                "JOIN users u ON p.user_id = u.user_id " + "WHERE v.doctor_id = ?";

        List<String[]> allVisits = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getVisitsStatement = conn.prepareStatement(visitsQuery)) {
            getVisitsStatement.setInt(1, doctorId);
            try (ResultSet rs = getVisitsStatement.executeQuery()) {
                while (rs.next()) {
                    String[] visitsRow = new String[6];
                    visitsRow[0] = String.valueOf(rs.getInt("visit_id"));
                    visitsRow[1] = String.valueOf(rs.getInt("patient_id"));
                    visitsRow[2] = rs.getString("full_name");
                    visitsRow[3] = rs.getString("notes");
                    visitsRow[4] = rs.getString("prescriptions");
                    visitsRow[5] = rs.getString("visit_date");
                    allVisits.add(visitsRow);
                }
            }
        }
        return allVisits;
    }


    /**
     * Updates the details of an existing visit record in the database.
     * @param visitId       the unique identifier of the visit to be updated.
     * @param notes         the updated notes for the visit.
     * @param prescriptions the updated prescriptions information for the visit.
     * @param visitDate     the updated date and time for the visit.
     */
    public void updateVisitDetails(int visitId, String notes, String prescriptions, LocalDateTime visitDate) throws SQLException {
        String updateQuery = "UPDATE visits SET notes = ?, prescriptions = ?, visit_date = ? WHERE visit_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateVisitStatement = conn.prepareStatement(updateQuery)) {

            updateVisitStatement.setString(1, notes);
            updateVisitStatement.setString(2, prescriptions);
            updateVisitStatement.setObject(3, visitDate);
            updateVisitStatement.setInt(4, visitId);

            int rowsAffected = updateVisitStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Update failed: Failed to update visit details");
            }
        }
    }
}
