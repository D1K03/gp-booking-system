package app.database;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Enters user credentials into user table after registration.
     * Additionally, adds a userId reference into another table depending on the role of user on registration.
     * @return userId of new user
     * @throws SQLException if SQL query fails due to database connectivity or invalid query
     */
    public int addUser(String username, String firstName, String lastName, String email, java.sql.Date dateOfBirth, String phone, String address, String postal, String city, String role, String password, Timestamp registrationDate) throws SQLException {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        String userQuery = "INSERT INTO users (username, first_name, last_name, email, date_of_birth, phone_number, address, postcode, city, role, password_hash, reg_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String doctorQuery = "INSERT INTO doctors (user_id) VALUES (?)";
        String patientQuery = "INSERT INTO patients (user_id, doctor_id) VALUES (?, ?)";
        String randomDoctorQuery = "SELECT doctor_id FROM doctors ORDER BY RAND() LIMIT 1";

        int userId = -1;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement userStatement = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, username);
                userStatement.setString(2, firstName);
                userStatement.setString(3, lastName);
                userStatement.setString(4, email);
                userStatement.setDate(5, dateOfBirth);
                userStatement.setString(6, phone);
                userStatement.setString(7, address);
                userStatement.setString(8, postal);
                userStatement.setString(9, city);
                userStatement.setString(10, role);
                userStatement.setString(11, passwordHash);
                userStatement.setTimestamp(12, registrationDate);

                int affectedRows = userStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Registration Failed.");
                }
                try (ResultSet rs = userStatement.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getInt(1);
                    } else {
                        throw new SQLException("Registration Failed, Missing ID.");
                    }
                }
            }

            if ("doctor".equalsIgnoreCase(role)) {
                try (PreparedStatement doctorStatement = conn.prepareStatement(doctorQuery)) {
                    doctorStatement.setInt(1, userId);
                    doctorStatement.executeUpdate();
                }
            } else if ("patient".equalsIgnoreCase(role)) {
                int randomDoctorId = -1;
                try (PreparedStatement randomDoctorStmt = conn.prepareStatement(randomDoctorQuery);
                     ResultSet rs = randomDoctorStmt.executeQuery()) {
                    if (rs.next()) {
                        randomDoctorId = rs.getInt(1);
                    } else {
                        throw new SQLException("No doctors available to assign.");
                    }
                }

                try (PreparedStatement patientStatement = conn.prepareStatement(patientQuery)) {
                    patientStatement.setInt(1, userId);
                    patientStatement.setInt(2, randomDoctorId);
                    patientStatement.executeUpdate();
                }
            }

            conn.commit();
        }
        return userId;
    }



    /**
     * On Login checks whether username matches in the database along with the password hash.
     * @return boolean indicating of a match.
     */
    public boolean checkUserCredentials(String username, String password) throws SQLException {
        String query = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkCredStatement = conn.prepareStatement(query)) {
            checkCredStatement.setString(1,username);
            ResultSet rs = checkCredStatement.executeQuery();
            if (rs.next()) {
                String passwordHash = rs.getString("password_hash");
                return BCrypt.checkpw(password, passwordHash);
            }
        }
        return false;
    }

    /**
     * Verifying whether an email entered in registration already exists in database.
     * @param email checking this email to prevent duplication.
     * @return boolean indicating whether the email exists.
     */
    public boolean checkEmail(String email) throws SQLException {
        String query = "SELECT email FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkEmailStatement = conn.prepareStatement(query)) {
            checkEmailStatement.setString(1, email);
            ResultSet rs = checkEmailStatement.executeQuery();
            return rs.next();
        }
    }


    /**
     * Used to populate UserTable JTable by accessing users information from database with patients role.
     * @return A list of all records
     */
    public List<String[]> getAllUsers() throws SQLException {
        List<String[]> users = new ArrayList<>();
        String query = "SELECT user_id, username, first_name, last_name, email, date_of_birth, phone_number, address, postcode, city FROM users WHERE role = 'patient'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getAllUsersStatement = conn.prepareStatement(query);
             ResultSet rs = getAllUsersStatement.executeQuery()) {
            while (rs.next()) {
                String[] user = {
                        rs.getString("user_id"),
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("date_of_birth"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getString("postcode"),
                        rs.getString("city"),
                };
                users.add(user);
            }
        }
        return users;
    }

    /**
     * Gets the role of the user, identified by their username.
     * @return position of unique username, e.g. Doctor or Patient.
     */
    public String getRole(String username) throws SQLException {
        String query = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getRoleStatement = conn.prepareStatement(query)) {
            getRoleStatement.setString(1, username);
            ResultSet rs = getRoleStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        }
        return null;
    }

    /**
     * Gets UserId of user, uniquely identified by username.
     * @return UserId
     */
    public int getUserId (String username) throws SQLException {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getIdStatement = conn.prepareStatement(query)) {
            getIdStatement.setString(1, username);
            ResultSet rs = getIdStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }

    /**
     * Gets first name of user, uniquely identified by username.
     * @return First Name
     */
    public String getUserFirstName(String username) throws SQLException {
        String query = "SELECT first_name FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getFirstNameStatement = conn.prepareStatement(query)) {
            getFirstNameStatement.setString(1, username);
            ResultSet rs = getFirstNameStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("first_name");
            }
        }
        return null;
    }

    /**
     * Gets last name of user, uniquely identified by username.
     * @return Last Name
     */
    public String getUserLastName(String username) throws SQLException {
        String query = "SELECT last_name FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getLastNameStatement = conn.prepareStatement(query)) {
            getLastNameStatement.setString(1, username);
            ResultSet rs = getLastNameStatement.executeQuery();
            if (rs.next()) {
                return rs.getString("last_name");
            }
        }
        return null;
    }


    /**
     * Retrieves the user's Id along with their full name.
     * @param role the position of the user in the system, e.g. Doctor or Patient.
     * @return String array of all the users of the role passed as argument
     */
    public String[] getUsersByRole(String role) throws SQLException {
        String roleQuery = "SELECT user_id, CONCAT(first_name, ' ', last_name) AS full_name FROM users WHERE role = ?";
        List<String> userRecords = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getRoleStatement = conn.prepareStatement(roleQuery)) {

            getRoleStatement.setString(1, role);
            try (ResultSet rs = getRoleStatement.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String fullName = rs.getString("full_name");
                    userRecords.add(userId + " " + fullName);
                }
            }
        }
        return userRecords.toArray(new String[0]);
    }
}
