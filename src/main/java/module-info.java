module name {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // exports projects.farmersbay.testfxml.old;
    exports projects.farmersbay;
    exports projects.farmersbay.view.Auth.Admin;
    exports projects.farmersbay.view.Auth;
    exports projects.farmersbay.controller.Admin;
    exports projects.farmersbay.model;
    exports projects.farmersbay.view.Admin;
    exports projects.farmersbay.view.Auth.Public;
    exports projects.farmersbay.controller.Public;
    exports projects.farmersbay.controller;
    // exports projects.farmersbay.view;

    
    // opens projects.farmersbay.testfxml.old to javafx.fxml;
    opens projects.farmersbay to javafx.fxml;
    opens projects.farmersbay.view.Auth.Admin to javafx.fxml;
    opens projects.farmersbay.view.Auth to javafx.fxml;
    opens projects.farmersbay.controller.Admin to javafx.fxml;
    opens projects.farmersbay.model to javafx.fxml;
    opens projects.farmersbay.view.Admin to javafx.fxml;
    opens projects.farmersbay.view.Auth.Public to javafx.fxml;
    opens projects.farmersbay.controller.Public to javafx.fxml;
    opens projects.farmersbay.controller to javafx.fxml;
    // opens projects.farmersbay.view to javafx.fxml;
   
}
