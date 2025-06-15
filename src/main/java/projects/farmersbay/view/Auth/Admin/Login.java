package projects.farmersbay.view.Auth.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Admin;

public class Login {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Auth/Admin/Login.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("FarmersBay");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.centerOnScreen();
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
        Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/Admin/SignUp.fxml"));
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
        Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/Admin/Option.fxml"));
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
        Admin admin = auth.login(name, password);

        if (admin != null) {
             showAlert("Login Berhasil", "Selamat datang, " + admin.getName() + "!");
            AuthController.currentAdminId = admin.getId();

            try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Dashboard/AdminPage.fxml"));
            Parent dashboardRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(dashboardRoot));
            stage.setTitle("Dashboard FarmersBay");
            stage.show();
            } catch (Exception e) {
            e.printStackTrace();
        }
            // ItemCRUD crud = new ItemCRUD();
            // crud.showMenu(); 
        } else {
        showAlert("Login Gagal", "Username atau password salah.");
        }
    }
}
