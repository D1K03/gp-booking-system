package app.service;

import app.database.LogDAO;

public class LogService {
    private LogDAO logData = new LogDAO();

    /**
     * Calls the addLog method from the Log Data Access Object class (LogDAO) to log a new action.
     * Catches SQL Exception if query fails.
     * @return whether the log was added successfully or not
     */
    public boolean addLog(int userId, String action) {
        try {
            logData.addLog(userId, action);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}