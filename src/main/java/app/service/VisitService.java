package app.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import app.database.VisitDAO;

public class VisitService {
    private VisitDAO visitData = new VisitDAO();

    /**
     * Calls the insertVisit method in the Visit Data Access Object class (VisitDAO) to add new visit details.
     * Catches SQL Exception if query fails.
     */
    public void insertVisit(int patientId, int doctorId, String notes, String prescriptions, LocalDateTime visitDate) {
        try {
            visitData.insertVisit(patientId, doctorId, notes, prescriptions, visitDate);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Calls the getVisits method in the Visit Data Access Object class (VisitDAO) to retrieve all visits information.
     * Catches SQL Exception if query fails.
     * @return List of string arrays containing visit information
     */
    public List<String[]> getVisits(int doctorId) {
        try {
            return visitData.getVisits(doctorId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the updateVisitDetails method in the Visit Data Access Object class (VisitDAO) to update a current visit detail.
     * Catches SQL Exception if query fails.
     */
    public void updateVisitDetails(int visitId, String notes, String prescriptions, LocalDateTime visitDate) {
        try {
            visitData.updateVisitDetails(visitId, notes, prescriptions, visitDate);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
