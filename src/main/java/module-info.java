module com.example.handlethatpos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.naming;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.xml.bind;
    requires bcrypt;
    requires java.desktop;
    requires javafx.swing;
    exports view;
    opens model.classes;
    opens view;
    exports model.classes;
    exports model.DAOs;
    opens model.DAOs;
}