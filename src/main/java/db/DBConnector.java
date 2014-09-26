package db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * General class for communicating with a mySQl database server
 *
 * Created by Andreas Øium. Imported by Audun Sutterud on 9/24/14.
 */

public class DBConnector {
    protected Connection connection;
    protected PreparedStatement preparedStatement;
    protected boolean connected;

    // Variables to get connection information from a file
    private Properties properties;
    private FileInputStream inputStream;
    private String url;
    private String user;
    private String password;

    public DBConnector() {
        connection = null;
        preparedStatement = null;
        connected = false;

        // Get connection information from file
        properties = new Properties();
        try {
            inputStream = new FileInputStream("database.properties");
            properties.load(inputStream);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Connects to the mySQL database server
     * @return true if connection was successful
     */
    public boolean connect() {
        if (connected) {
            System.err.println("You are already connected!");
            return false;
        }
// Check for valid inputs
        if (!validateInput()) {
            System.err.println("Properties validation not succesful, check your properties!");
            return false;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            connected = true;
            System.out.println("Connection to database has been established.");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Could not find specified JDBC Driver class! ");
            e.printStackTrace();
            return false;
        }
//        catch (CommunicationsException e) {
//            System.err.println("Cannot connect to database server! Check your connection properties and check that the server is running! ");
//            e.printStackTrace();
//            return false;
//        }
        catch (SQLException e) {
            System.err.println("Access denied! Check your username and password! ");
            e.printStackTrace();
            return false;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Closes connection to the database server
     */
    public void disconnect() {
        if (!connected) {
            System.err.println("You are not connected!");
            return;
        }
        try {
            if (preparedStatement != null)
                preparedStatement.close();
            if (connection != null)
                connection.close();
            connected = false;
        }
        catch (Exception e){
            System.err.println("Error encountered while disconnecting!");
            e.printStackTrace();
        }
    }

    /**
     * Updates the connection properties. Should only be used when switching databases and/or users
     * while program is running. Remember to disconnect first!
     */
    public void updateProperties() {
        if (connected) {
            System.out.println("You need to disconnect first!");
            return;
        }
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
    }

    /**
     * Checks if a connection is active.
     * @return connected
     */
    public boolean isConnected() {
        return connected;
    }

/* PROTECTED METHODS */
    /**
     * Closes the statement
     */
    protected void closeStatement() {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initiates a rollback
     */
    protected void tryRollback() {
        if (connection != null) {
            try {
                System.out.println("Rollback initiated.");
                connection.rollback();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

/* PRIVATE METHODS */
    /**
     * Checks for valid database properties
     * @return true if valid, false otherwise
     */
    private boolean validateInput() {
        if (url == null || user == null || password == null) {
            return false;
        }
        return true;
    }
}
