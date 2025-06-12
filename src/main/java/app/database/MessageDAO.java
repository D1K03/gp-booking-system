package app.database;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class MessageDAO {

    /**
     * Sends a message by inserting a new row into the messages table.
     * @param senderId   The user ID of the sender (e.g., the doctor).
     * @param receiverId The user ID of the receiver (e.g., admin or receptionist).
     * @param message    The message content.
     * @throws SQLException if SQL query fails due to database connectivity or invalid query.
     */
    public void sendMessage(int senderId, int receiverId, String message) throws SQLException {
        String sql = "INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement sendMessageStatement = conn.prepareStatement(sql)) {

            sendMessageStatement.setInt(1, senderId);
            sendMessageStatement.setInt(2, receiverId);
            sendMessageStatement.setString(3, message);

            int affectedRows = sendMessageStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Sending message failed, no rows affected.");
            }
        }
    }
}
