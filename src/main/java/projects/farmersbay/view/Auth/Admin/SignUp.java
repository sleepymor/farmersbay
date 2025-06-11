package projects.farmersbay.view.Auth.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Admin;

public class SignUp {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Auth/Admin/SignUp.fxml"));
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
    private void handleLoginClick(javafx.scene.input.MouseEvent event) {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/Admin/Login.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("FarmersBay");
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        showAlert("Error", "Gagal memuat halaman login.");
        }
    }

    @FXML
    private void handleSignUp(javafx.scene.input.MouseEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Admin admin = new Admin();
        admin.setName(username);
        admin.setPassword(password);

        AuthController auth = new AuthController();
        auth.create(admin);

        showAlert("Sukses", "Akun berhasil dibuat!");

        usernameField.clear();
        passwordField.clear();

    }
}