package app.database;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import io.github.cdimascio.dotenv.Dotenv;

public class DBConnection {

    /**
     * Uses JDBC to connect to database, gets url and credentials of database from env file to establish connection.
     * @return A Connection if credentials are correct.
     * @throws SQLException if connection to database fails.
     */
    public static Connection getConnection() throws SQLException {
        Dotenv dotenv = Dotenv.load();

        String db_url = dotenv.get("DB_URL");
        String db_user = dotenv.get("DB_USER");
        String db_password = dotenv.get("DB_PASSWORD");

        if (db_url == null || db_user == null || db_password == null) {
            throw new SQLException("Missing Database Credentials");
        }

        return DriverManager.getConnection(db_url, db_user, db_password);
    }
}
