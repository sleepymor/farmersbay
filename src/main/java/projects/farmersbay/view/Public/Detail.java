package projects.farmersbay.view.Public;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

public class Detail {
    @FXML
    private void handleUserClick(MouseEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Konfirmasi Logout");
    alert.setHeaderText("Anda yakin ingin keluar?");
    alert.setContentText("Klik OK untuk logout, atau Cancel untuk tetap di halaman ini.");
}
}
