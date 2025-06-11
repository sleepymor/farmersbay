package projects.farmersbay.view.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import projects.farmersbay.model.Items;
import projects.farmersbay.controller.Admin.ItemsController;
import projects.farmersbay.controller.Admin.AuthController;
import projects.farmersbay.model.Category;
import projects.farmersbay.controller.Admin.CategoryController;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class ItemCRUD implements Initializable {

    @FXML private TextField nameField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextArea descField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private Button Photo;
    @FXML private Button save;

    private File selectedImageFile;
    private final ItemsController itemsController = new ItemsController();
    private final CategoryController categoryController = new CategoryController();

    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    List<Category> categories = categoryController.readAll();  
    categoryComboBox.getItems().addAll(categories);

    categoryComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
        @Override
        protected void updateItem(Category category, boolean empty) {
            super.updateItem(category, empty);
            setText(empty || category == null ? null : category.getTitle());
        }
    });
    categoryComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
        @Override
        protected void updateItem(Category category, boolean empty) {
            super.updateItem(category, empty);
            setText(empty || category == null ? null : category.getTitle());
        }
    });

    priceField.setText("Rp.");
    priceField.textProperty().addListener((obs, oldVal, newVal) -> {
        if (!newVal.startsWith("Rp.")) {
            priceField.setText("Rp.");
        }
    });
}

    @FXML
    private void handleBrowseImage(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) Photo.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Image Upload");
        alert.setHeaderText(null);
        alert.setContentText("Foto berhasil ditambahkan!");
        alert.showAndWait();
        }
    }

    @FXML
private void handleSave(MouseEvent event) {
    String name = nameField.getText();
    String priceText = priceField.getText().replace("Rp.", "").trim();
    String stockText = stockField.getText();
    String desc = descField.getText();
    Category selectedCategory = categoryComboBox.getValue(); 
    if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty() || selectedCategory == null || selectedImageFile == null) {
        System.out.println("Please complete all fields");
         {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Tidak Lengkap");
        alert.setHeaderText(null);
        alert.setContentText("Harap lengkapi semua field terlebih dahulu!");
        alert.showAndWait();
        return;
    }
    }

    Items item = new Items();
    item.setTitle(name);
    item.setPrice(Double.parseDouble(priceText));
    item.setStock(Integer.parseInt(stockText));
    item.setDescription(desc);
    item.setCategoryName(selectedCategory.getTitle());
    item.setImg(selectedImageFile.toURI().toString());
    item.setAdminId(AuthController.currentAdminId);

    itemsController.create(item);
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Berhasil");
    alert.setHeaderText(null);
    alert.setContentText("Item berhasil ditambahkan!");
    alert.showAndWait();

    if (Dashboard.staticTableRef != null) {
        Dashboard.staticTableRef.getItems().add(item);
    }

    ((Stage) save.getScene().getWindow()).close();
}
    @FXML
    private void validateNumber(MouseEvent event) {
      
    }

    @FXML
    private void handleExit(MouseEvent event) {
        ((Stage) save.getScene().getWindow()).close();
    }

    
}