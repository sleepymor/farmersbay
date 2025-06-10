package projects.farmersbay.old.testfxml;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class signupcontroller {
    
    @FXML
    private void handleloginClick(MouseEvent event) {
        loadScene(event, "/ui/Login.fxml");
    }
    @FXML
    private void handleExit(MouseEvent event) {
        loadScene(event, "/ui/chosseuser.fxml");
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
