module com.example.handlethatpos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens view to javafx.fxml;
    exports view;
}