package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection getDBConnection() throws SQLException {
        String JDBC_URL = "jdbc:postgresql://localhost:5432/mini_football_db";
        String USER = "mini_football_db_manager";
        String PASS = "password";
        return DriverManager.getConnection(JDBC_URL, USER, PASS);
    }
}
