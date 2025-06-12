package app.service;

import app.database.PatientDAO;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PatientService {
    private PatientDAO patientData = new PatientDAO();


    /**
     * Calls the updatePatientDoctor method from the Patient Data Access Object class (PatientDAO) to assign the patient a new doctor.
     * Catches SQL Exception if query fails.
     */
    public void updatePatientDoctor(int patientUserId, int newDoctorId) {
        try {
            patientData.updatePatientDoctor(patientUserId, newDoctorId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Calls the getMyUsers method from the Patient Data Access Object class (PatientDAO) to get user information of a doctors patients.
     * Catches SQL Exception if query fails.
     * @return List of string arrays for the doctors patients
     */
    public List<String[]> getMyUsers(int doctorId) {
        try {
            return patientData.getMyUsers(doctorId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the getMyPatients method from the Patient Data Access Object class (PatientDAO) to get patient information of a doctors patients.
     * Catches SQL Exception if query fails.
     * @return String array of the doctors patients
     */
    public String[] getMyPatients(int doctorId) {
        try {
            return patientData.getMyPatients(doctorId);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[0];
        }
    }
}
