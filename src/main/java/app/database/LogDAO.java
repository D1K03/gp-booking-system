package app.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogDAO {

    /**
     * Inserts a new log record into the logs table.
     * @param userId the id of the user performing the action.
     * @param action a description of the action.
     * @throws SQLException if SQL query fails due to database connectivity or invalid query.
     */
    public void addLog(int userId, String action) throws SQLException {
        String query = "INSERT INTO logs (user_id, action) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement addLogStatement = conn.prepareStatement(query)) {

            addLogStatement.setInt(1, userId);
            addLogStatement.setString(2, action);

            addLogStatement.executeUpdate();
        }
    }
}