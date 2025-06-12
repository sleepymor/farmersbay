package projects.farmersbay.view.Admin;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import projects.farmersbay.controller.Admin.ItemsController;
import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Items;

public class Dashboard {

    @FXML
    private VBox mainPane;
    
    @FXML private Pane Tabel;

    public static TableView<Items> staticTableRef;

    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }

    public void setupTable() {
    TableView<Items> tableView = new TableView<>();
    staticTableRef = tableView;

    tableView.setPrefSize(620, 430);

        // Gambar
        TableColumn<Items, String> colTitle = new TableColumn<>("Name");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setPrefWidth(150);

        // Nama
        TableColumn<Items, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        // Harga (Rp.)
        TableColumn<Items, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cellData -> {
            double harga = cellData.getValue().getPrice();
            return new ReadOnlyStringWrapper("Rp. " + harga);
        });

        // Stok
        TableColumn<Items, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Deskripsi
        TableColumn<Items, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Kategori
        TableColumn<Items, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        tableView.getColumns().addAll(imageCol, nameCol, priceCol, stockCol, descCol, catCol);
        Tabel.getChildren().add(tableView);
    }

    @FXML
    private void handleLogoutClick(javafx.scene.input.MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/Auth/ChooseUser.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FarmersBay");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();}

    @FXML
    private void handleLoadCrud(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Dashboard/CRUD.fxml"));
        Parent popupRoot = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle("Kategori");
        popupStage.setScene(new Scene(popupRoot));
        popupStage.initOwner(((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow())); // agar terkait window utama
        popupStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    @FXML
    private void handleCategoryCrud(MouseEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Dashboard/CategoryMenu.fxml"));
        Parent popupRoot = loader.load();

        Stage popupStage = new Stage();
        popupStage.setTitle("Kategori");
        popupStage.setScene(new Scene(popupRoot));
        popupStage.initOwner(((Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow())); // agar terkait window utama
        popupStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
