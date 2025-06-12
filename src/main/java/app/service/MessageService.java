package app.service;

import app.database.MessageDAO;
import java.sql.SQLException;

public class MessageService {
    private final MessageDAO messageDAO = new MessageDAO();

    /**
     * Calls the sendMessage method from the Message Data Access Object class (MessageDAO) to send a message.
     * Catches SQL Exception if query fails.
     */
    public void sendMessage(int senderId, int receiverId, String message) {
        try {
            messageDAO.sendMessage(senderId, receiverId, message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

