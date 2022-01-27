module com.example.handlethatpos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.handlethatpos to javafx.fxml;
    exports com.example.handlethatpos;
}