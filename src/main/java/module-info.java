module com.example.sdis_lab2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.sdis_lab2 to javafx.fxml;
    exports com.example.sdis_lab2;
}