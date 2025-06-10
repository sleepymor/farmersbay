package projects.farmersbay.testfxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class controllerchosseuser {

    
    @FXML
    private Button admin;

    @FXML
    private Button user;

    @FXML
    private void handleAdminClick(MouseEvent event) {
        loadScene(event, "/ui/optionAdmin.fxml");
    }

    @FXML
    private void handleUserClick(MouseEvent event) {
        loadScene(event, "/ui/optionUser.fxml");
    }

     @FXML
    private void handleExit(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
     private void loadScene(MouseEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FarmersBay");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}