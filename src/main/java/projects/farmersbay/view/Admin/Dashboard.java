package projects.farmersbay.view.Admin;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import projects.farmersbay.controller.Admin.ItemsController;
import projects.farmersbay.model.Items;

public class Dashboard implements Initializable {

    @FXML
    private VBox mainPane;
    
    @FXML private Pane Tabel;

    public static TableView<Items> staticTableRef;
    
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
}

    public void setCategoryName(String categoryName) {
         this.categoryName = categoryName;
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
    }

    public void setupTable() {
    TableView<Items> tableView = new TableView<>();
    staticTableRef = tableView;
    tableView.setPrefSize(620, 488);
    ItemsController itemsController = new ItemsController();
    tableView.getItems().addAll(itemsController.readAll());
    Tabel.getChildren().add(tableView);

   
    TableColumn<Items, ImageView> imageCol = new TableColumn<>("Image");
    imageCol.setCellValueFactory(cellData -> {
        String imagePath = cellData.getValue().getImg();
        Image image = new Image(imagePath, 60, 60, true, true);
        ImageView imageView = new ImageView(image);
        return new ReadOnlyObjectWrapper<>(imageView);
    });
    imageCol.setPrefWidth(70);

    
    TableColumn<Items, String> nameCol = new TableColumn<>("Name");
    nameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
    nameCol.setPrefWidth(120);

 
    TableColumn<Items, String> priceCol = new TableColumn<>("Price");
    priceCol.setCellValueFactory(cellData -> {
        double harga = cellData.getValue().getPrice();
        return new ReadOnlyStringWrapper("Rp. " + harga);
    });
    priceCol.setPrefWidth(100);

    
    TableColumn<Items, Integer> stockCol = new TableColumn<>("Stock");
    stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    stockCol.setPrefWidth(80);

   
    TableColumn<Items, String> descCol = new TableColumn<>("Description");
    descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    descCol.setPrefWidth(150);

   
    TableColumn<Items, String> catCol = new TableColumn<>("Category");
    catCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
    catCol.setPrefWidth(100);

    tableView.getColumns().addAll(imageCol, nameCol, priceCol, stockCol, descCol, catCol);
    Tabel.getChildren().clear();
    Tabel.getChildren().add(tableView);
}
    @FXML
private void handleAddStock(MouseEvent event) {
    if (staticTableRef != null) {
        Items selectedItem = staticTableRef.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            // Input stok baru
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Tambah Stok");
            dialog.setHeaderText("Masukkan jumlah stok yang ingin ditambahkan:");
            dialog.setContentText("Jumlah:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(input -> {
                try {
                    int addAmount = Integer.parseInt(input);
                    if (addAmount > 0) {
                        ItemsController controller = new ItemsController();
                        controller.addStock(selectedItem.getItemId(), addAmount);

                        
                        setupTable();

                        System.out.println("Stock updated for: " + selectedItem.getTitle());
                    } else {
                        System.out.println("Jumlah stok harus lebih dari 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid, harus angka.");
                }
            });

        } else {
            System.out.println("Pilih item terlebih dahulu.");
        }
    }
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
    private void handleDeleteItem(MouseEvent event) {
    
         if (staticTableRef != null) {
        Items selectedItem = staticTableRef.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ItemsController controller = new ItemsController();

            controller.delete(selectedItem.getItemId());

            staticTableRef.getItems().remove(selectedItem);

            System.out.println("Item Terhapus: " + selectedItem.getTitle());
        } else {
            System.out.println("Tidak ada Item yang terpilih untuk di hapus.");
        }
        }
    }

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
