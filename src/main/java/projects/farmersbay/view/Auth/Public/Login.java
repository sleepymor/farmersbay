package projects.farmersbay.view.Auth.Public;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import projects.farmersbay.controller.Public.AuthController;
import projects.farmersbay.model.User;

public class Login {
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Auth/Public/Login.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("FarmersBay");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
    }

     @FXML
    private void handlesignClick(javafx.scene.input.MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/Public/SignUp.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("FarmersBay");
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Error", "Gagal kembali ke halaman untuk membuat akun.");
        }
    }

    @FXML
    private void handleCloseClick(javafx.scene.input.MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/Public/Option.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("FarmersBay");
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Error", "Gagal kembali ke halaman opsi.");
        }
    }

    @FXML
    private void handleLoginClick(MouseEvent event) {
        String name = usernameField.getText();
        String password = passwordField.getText();

            AuthController auth = new AuthController();
            User user = auth.login(name, password);    
            
            if (user != null) {
            showAlert("Login Berhasil", "Selamat datang, " + user.getName() + "!");
            AuthController.currentUserId = user.getId();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));
                Parent dashboardRoot = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(dashboardRoot));
                stage.setTitle("FarmersBay");
                stage.show();
            } catch (Exception e) {
            e.printStackTrace();
        }
                // Cart cart = new Cart();
                // cart.show();
            } else {
              showAlert("Login Gagal", "Username atau password salah.");
            }
        }
    }
