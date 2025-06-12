package app.service;

import app.database.DoctorDAO;

import java.sql.SQLException;
import java.util.List;

public class DoctorService {
    private DoctorDAO doctorData = new DoctorDAO();

    /**
     * Calls the getAllDoctors method from the Doctor Data Access Object class (DoctorDAO) to get all registered doctors.
     * Catches SQL Exception if query fails.
     * @return String Array of all doctors
     */
    public String[] getAllDoctors() {
        try {
            return doctorData.getAllDoctors();
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    /**
     * Calls the getUserSpeciality method in the Doctor Data Access Object class (DoctorDAO) to get the doctor's speciality
     * Catches SQL Exception if query fails.
     * @return the doctor's speciality
     */
    public String getUserSpeciality(int userId) {
        try {
            return doctorData.getUserSpeciality(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the updateDoctorSpeciality method in the Doctor Data Access Object class (DoctorDAO) to change the doctor's speciality.
     * Catches SQL Exception if query fails.
     * @param doctorId the doctor's id
     * @param specialty the new specialty to set
     * @return whether update was successful or not
     */
    public boolean updateDoctorSpecialty(int doctorId, String specialty) {
        try {
            doctorData.updateDoctorSpecialty(doctorId, specialty);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
