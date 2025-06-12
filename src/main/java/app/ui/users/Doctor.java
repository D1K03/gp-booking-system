package app.ui.users;

/**
 * Record that automatically creates getters for the doctor's information.
 * @param doctorId unique Id to identify each doctor.
 * @param firstName forename of doctor.
 * @param lastName surname of doctor.
 * @param speciality what each doctor specialises in.
 */
public record Doctor(int doctorId, String firstName, String lastName, String speciality) {
}
