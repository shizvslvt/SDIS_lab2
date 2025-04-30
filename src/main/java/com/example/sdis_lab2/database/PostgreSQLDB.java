package com.example.sdis_lab2.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class PostgreSQLDB {
    private Connection connection;

    public PostgreSQLDB() {
        try {
            String databaseUser = "postgres";
            String databasePassword = "12345";
            String databaseName = "postgres";
            String databaseHost = "localhost";
            String databasePort = "5432";
            String url = "jdbc:postgresql://" + databaseHost + ":" + databasePort + "/" + databaseName;
            connection = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
