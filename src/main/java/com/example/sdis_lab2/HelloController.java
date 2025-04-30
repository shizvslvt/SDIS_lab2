package com.example.sdis_lab2;

import com.example.sdis_lab2.controllers.MySQLController;
import com.example.sdis_lab2.controllers.PostgreSQLController;
import com.example.sdis_lab2.database.MySQLDB;
import com.example.sdis_lab2.database.PostgreSQLDB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class HelloController {

    @FXML private TextArea mysqlInput;
    @FXML private TableView<ObservableList<String>> mysql_table;
    @FXML private TextArea pgsqlInput;
    @FXML private TableView<ObservableList<String>> pgsql_table;
    private final MySQLController mySQLController = new MySQLController();
    private final PostgreSQLController postgreSQLController = new PostgreSQLController();
    @FXML private Label mysqlTimeLabel;
    @FXML private Label pgsqlTimeLabel;

    @FXML
    protected void onMySQLQuery() {
        String query = mysqlInput.getText().trim();
        long startTime = System.currentTimeMillis();
        if (query.toUpperCase().startsWith("SELECT")) {
            mySQLController.executeSelectQuery(mysqlInput, mysql_table, () -> showAlert("MySQL ERROR."));
        } else {
            mySQLController.executeUpdateQuery(mysqlInput, () -> showAlert("MySQL ERROR."));
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        mysqlTimeLabel.setText(duration + " ms");
    }

    @FXML
    protected void onPostgreSQLQuery() {
        String query = pgsqlInput.getText().trim();
        long startTime = System.currentTimeMillis();
        if (query.toUpperCase().startsWith("SELECT")) {
            postgreSQLController.executeSelectQuery(pgsqlInput, pgsql_table, () -> showAlert("PostgreSQL ERROR."));
        } else {
            postgreSQLController.executeUpdateQuery(pgsqlInput, () -> showAlert("PostgreSQL ERROR."));
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        pgsqlTimeLabel.setText(duration + " ms");
    }

    @FXML
    protected void onTransaction() {
        Connection mysqlConn = null;
        Connection pgsqlConn = null;

        String mysqlQuery = mysqlInput.getText().trim();
        String pgsqlQuery = pgsqlInput.getText().trim();

        if (mysqlQuery.isEmpty() || pgsqlQuery.isEmpty()) {
            showAlert("Оба поля запроса должны быть заполнены.");
            return;
        }



        try {
            mysqlConn = new MySQLDB().getConnection();
            pgsqlConn = new PostgreSQLDB().getConnection();

            mysqlConn.setAutoCommit(false);
            pgsqlConn.setAutoCommit(false);

            long startTime1 = System.currentTimeMillis();
            try (PreparedStatement stmt1 = mysqlConn.prepareStatement(mysqlQuery)) {
                stmt1.executeUpdate();
            }
            long endTime1 = System.currentTimeMillis();
            long duration1 = endTime1 - startTime1;

            long startTime2 = System.currentTimeMillis();
            try (PreparedStatement stmt2 = pgsqlConn.prepareStatement(pgsqlQuery)) {
                stmt2.executeUpdate();
            }
            long endTime2 = System.currentTimeMillis();
            long duration2 = endTime2 - startTime2;

            mysqlConn.commit();
            pgsqlConn.commit();

            mysqlTimeLabel.setText(duration1 + " ms");
            pgsqlTimeLabel.setText(duration2 + " ms");

        } catch (Exception e) {
            e.printStackTrace();

            try {
                if (mysqlConn != null) mysqlConn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (pgsqlConn != null) pgsqlConn.rollback();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            showAlert("Ошибка при выполнении транзакции. Все изменения отменены.");

        } finally {

            mySQLController.executeSelectQuery(new TextArea("SELECT * FROM ro_users"), mysql_table, () -> showAlert("MySQL ERROR при отображении."));
            postgreSQLController.executeSelectQuery(new TextArea("SELECT * FROM ro_users"), pgsql_table, () -> showAlert("PostgreSQL ERROR при отображении."));

            try {
                if (mysqlConn != null) mysqlConn.close();
                if (pgsqlConn != null) pgsqlConn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
