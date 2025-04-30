package com.example.sdis_lab2.controllers;

import com.example.sdis_lab2.database.MySQLDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

import java.sql.*;

public class MySQLController {

    public void executeSelectQuery(TextArea input, TableView<ObservableList<String>> table, Runnable onError) {
        table.getItems().clear();
        table.getColumns().clear();

        String query = input.getText();

        try (Connection conn = new MySQLDB().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();

            for (int i = 1; i <= cols; i++) {
                final int colIndex = i - 1;
                TableColumn<ObservableList<String>, String> col = new TableColumn<>(meta.getColumnName(i));
                col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
                table.getColumns().add(col);
            }

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= cols; i++) {
                    row.add(rs.getString(i));
                }
                table.getItems().add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
            onError.run();
        }
    }

    public void executeUpdateQuery(TextArea input, Runnable onError) {
        String query = input.getText();

        try (Connection conn = new MySQLDB().getConnection();
             Statement stmt = conn.createStatement()) {

            int result = stmt.executeUpdate(query);
            System.out.println("Rows affected: " + result);

        } catch (Exception e) {
            e.printStackTrace();
            onError.run();
        }
    }

}
