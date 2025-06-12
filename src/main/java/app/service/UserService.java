package app.service;

import app.database.UserDAO;

import java.sql.*;
import java.util.List;

public class UserService {
    private UserDAO userData = new UserDAO();


    /**
     * Calls the addUser method in the User Data Access Object class (UserDAO) to add new user.
     * Catches SQL Exception if query fails.
     * @return the user's Id
     */
    public int addUser(String username, String firstName, String lastName, String email, java.sql.Date dateOfBirth, String phone, String address, String postal, String city, String role, String password, Timestamp registrationDate) {
        if(username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || dateOfBirth == null ||
                password.isEmpty() || phone.isEmpty() || address.isEmpty() || postal.isEmpty() || city.isEmpty() || role.isEmpty()) {
            throw new IllegalArgumentException("One or more required fields are empty");
        }

        try {
            return userData.addUser(username, firstName, lastName, email, dateOfBirth, phone, address, postal, city, role, password, registrationDate);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Calls the checkEmail method in the User Data Access Object class (UserDAO) to check whether entered email exists.
     * Catches SQL Exception if query fails.
     * @return whether that email address has already been used by another user
     */
    public boolean checkEmail(String email) {
        try {
            return userData.checkEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calls the checkUserCredentials method in the User Data Access Object class (UserDAO) to validate user's authentication.
     * Catches SQL Exception if query fails.
     * @return whether user has entered correct login details
     */
    public boolean checkUserCredentials(String username, String password) {
        try {
            return userData.checkUserCredentials(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Calls the getRole method in the User Data Access Object class (UserDAO) to get user's role.
     * Catches SQL Exception if query fails.
     * @return the user's role
     */
    public String getRole(String username) {
        try {
            return userData.getRole(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the getUserId method in the User Data Access Object class (UserDAO) to get user's userId.
     * Catches SQL Exception if query fails.
     * @return the user's userId
     */
    public int getUserId(String username) {
        try {
            return userData.getUserId(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Calls the getUserFirstName method in the User Data Access Object class (UserDAO) to get user's first name.
     * Catches SQL Exception if query fails.
     * @return the user's forename
     */
    public String getUserFirstName(String username) {
        try {
            return userData.getUserFirstName(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the getUserLastName method in the User Data Access Object class (UserDAO) to get user's last name.
     * Catches SQL Exception if query fails.
     * @return the user's surname
     */
    public String getUserLastName(String username) {
        try {
            return userData.getUserLastName(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Calls the getAllUsers method in the User Data Access Object class (UserDAO) to retrieve all users.
     * Catches SQL Exception if query fails.
     * @return List of string arrays containing each record of user data in the database.
     */
    public List<String[]> getAllUsers() {
        try {
            return userData.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls the getUsersByRole method in the User Data Access Object class (UserDAO) to get users of specified role.
     * Catches SQL Exception if query fails.
     * @return String Array of users with specified role
     */
    public String[] getUsersByRole(String role) {
        try {
            return userData.getUsersByRole(role);
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new String[0];
        }
    }
}