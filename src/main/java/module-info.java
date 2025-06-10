module name {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    
    opens projects.farmersbay.testfxml to javafx.fxml;
    exports projects.farmersbay.testfxml;
    exports projects.farmersbay;
    opens projects.farmersbay to javafx.fxml;
}
