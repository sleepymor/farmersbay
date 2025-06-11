package projects.farmersbay.controller.Public;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class OptionController {
    
    @FXML
    private Button login;

    @FXML
    private Button sign;

    @FXML
    private void handleLoginClick(MouseEvent event) {
        loadScene(event, "/ui/Auth/Public/Login.fxml");
    }

    @FXML
    private void handleSignClick(MouseEvent event) {
        loadScene(event, "/ui/Auth/Public/SignUp.fxml");
    }

    @FXML
    private void handleExit(MouseEvent event) {
        loadScene(event, "/ui/Auth/ChooseUser.fxml");
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
