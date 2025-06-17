package projects.farmersbay.view.Public;


import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projects.farmersbay.model.Items;

public class Detail {
    
    @FXML
    private ImageView Image;

    @FXML
    private Text Title, Price, Stock, Desc;

    @FXML
    private Button User;

    public void setProductData(Items item) {
        Title.setText(item.getTitle());
        Price.setText("Rp. " + item.getPrice());
        Stock.setText("Stok: " + item.getStock());
        Desc.setText(item.getDescription());
        
        if (item.getImg() != null && !item.getImg().isEmpty()) {
            Image.setImage(new Image("file:" + item.getImg()));
        }
    }

    @FXML
private void handlemainClick(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("FarmersBay");
        currentStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@FXML
private void handleback(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));
        Parent root = loader.load();

        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(root));
        currentStage.setTitle("FarmersBay");
        currentStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void handleUserClick(MouseEvent event) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Konfirmasi Logout");
    alert.setHeaderText("Anda yakin ingin keluar?");
    alert.setContentText("Klik OK untuk logout, atau Cancel untuk tetap di halaman ini.");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Auth/ChooseUser.fxml"));
            Parent loginRoot = loader.load();

            Stage stage = (Stage) User.getScene().getWindow();  
            Scene scene = new Scene(loginRoot);

            stage.setScene(scene);
            stage.sizeToScene();

            stage.setMinWidth(320);
            stage.setMinHeight(450);
            stage.setResizable(true);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
}
