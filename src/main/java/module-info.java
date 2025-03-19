module com.example.asteroidsapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.asteroidsapp to javafx.fxml;
    exports com.example.asteroidsapp;
}