package com.example.sdis_lab2.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLDB {
    private Connection connection;

    public MySQLDB() {
        try {
            String databaseUser = "root";
            String databasePassword = "";
            String databaseName = "realtor_office";
            String url = "jdbc:mysql://localhost/" + databaseName;
            connection = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public Connection getConnection() {
        return connection;
    }
}