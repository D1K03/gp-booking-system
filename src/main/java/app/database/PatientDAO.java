package app.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {


    /**
     * Retrieves all patients information (their Id and full name) associated with a given doctor from the database.
     * @param doctorId the unique Id for the doctor whose patients are to be retrieved.
     * @return String Array representing the patient records for the specified doctor.
     * @throws SQLException if SQL query fails due to database connectivity or invalid query
     */
    public String[] getMyPatients(int doctorId) throws SQLException {

        String patientQuery = "SELECT p.patient_id, CONCAT(u.first_name, ' ', u.last_name) AS full_name " +
                "FROM users u " + "JOIN patients p ON u.user_id = p.user_id " + "WHERE u.role = 'patient' AND p.doctor_id = ?";

        List<String> patientRecords = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getMyPatientsStatement = conn.prepareStatement(patientQuery)) {
             getMyPatientsStatement.setInt(1, doctorId);

             try (ResultSet rs = getMyPatientsStatement.executeQuery()) {
                while (rs.next()) {
                    int patientId = rs.getInt("patient_id");
                    String fullName = rs.getString("full_name");
                    String record = patientId + " " + fullName;
                    patientRecords.add(record);
                }
            }
        }
        return patientRecords.toArray(new String[0]);
    }


    /**
     * Updates the doctor assignment for a given patient.
     * @param patientUserId the unique Id of the patient whose doctor is being updated.
     * @param newDoctorId   the unique Id of the new doctor to assign to the patient.
     */
    public void updatePatientDoctor(int patientUserId, int newDoctorId) throws SQLException {
        String updateQuery = "UPDATE patients SET doctor_id = ? WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updatePatientStatement = conn.prepareStatement(updateQuery)) {
            updatePatientStatement.setInt(1, newDoctorId);
            updatePatientStatement.setInt(2, patientUserId);
            int rowsAffected = updatePatientStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Update failed: No patient found with user_id " + patientUserId);
            }
        }
    }


    /**
     * Retrieves detailed information for all users (patients) associated with a specific doctor.
     * @param doctorId the unique Id of the doctor whose associated patient user details are to be retrieved.
     * @return a list of string arrays, where each array contains user details.
     */
    public List<String[]> getMyUsers(int doctorId) throws SQLException {
        String myUsersQuery = "SELECT u.user_id, u.username, u.first_name, u.last_name, u.email, " +
                "u.date_of_birth, u.phone_number, u.address, u.postcode, u.city " + "FROM users u " +
                "JOIN patients p ON u.user_id = p.user_id " + "WHERE p.doctor_id = ?";
        List<String[]> myUsers = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getMyUsersStatement = conn.prepareStatement(myUsersQuery)) {
            getMyUsersStatement.setInt(1, doctorId);
            try (ResultSet rs = getMyUsersStatement.executeQuery()) {
                while (rs.next()) {
                    String[] userRow = new String[10];
                    userRow[0] = String.valueOf(rs.getInt("user_id"));
                    userRow[1] = rs.getString("username");
                    userRow[2] = rs.getString("first_name");
                    userRow[3] = rs.getString("last_name");
                    userRow[4] = rs.getString("email");
                    userRow[5] = rs.getString("date_of_birth");
                    userRow[6] = rs.getString("phone_number");
                    userRow[7] = rs.getString("address");
                    userRow[8] = rs.getString("postcode");
                    userRow[9] = rs.getString("city");
                    myUsers.add(userRow);
                }
            }
        }
        return myUsers;
    }
}
