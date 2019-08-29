package Util;
/*
 * @author Tacet Nox
 *
 * SQL Database connector for use in bots
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnect {
    // init database constants
    private static final String DATABASE_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/tritemare?useSSL=false&allowPublicKeyRetrieval=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=EST";
    private static final String USERNAME = "tacet";
    private static final String PASSWORD = "INSERTPASS"; // UPDATE PRIOR TO SHADOWJAR


    // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
        }
        return properties;
    }

    // connect to database
    public Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
