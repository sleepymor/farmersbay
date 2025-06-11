package projects.farmersbay.view.Admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

import projects.farmersbay.controller.Admin.CategoryController;
import projects.farmersbay.model.Category;

public class CategoryCRUD {

    @FXML private TextField addField;
    @FXML private TextField searchField;
    @FXML private TableView<Category> tabelcategory;
    @FXML private TableColumn<Category, Integer> colId;
    @FXML private TableColumn<Category, String> colTitle;

    private final CategoryController categoryController = new CategoryController();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("categoryID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        loadTable();
        setupRowClickForUpdate();
    }


    private void loadTable() {
        List<Category> categories = categoryController.readAll();
        tabelcategory.setItems(FXCollections.observableArrayList(categories));
    }

    @FXML
    private void handleAddCategory() {
        String title = addField.getText().trim();
        if (!title.isEmpty()) {
            Category category = new Category();
            category.setTitle(title);
            categoryController.create(category);
            showAlert("Success", "Category added.");
            addField.clear();
            loadTable();
        } else {
            showAlert("Error", "Title cannot be empty.");
        }
    }

    @FXML
    private void handleDeleteCategory() {
        Category selected = tabelcategory.getSelectionModel().getSelectedItem();
        if (selected != null) {
            categoryController.delete(selected.getCategoryID());
            showAlert("Deleted", "Category deleted.");
            loadTable();
        } else {
            showAlert("Warning", "Select a category to delete.");
        }
    }

    @FXML
    private void handleSearchCategory() {
        String keyword = searchField.getText().toLowerCase();
        List<Category> categories = categoryController.readAll().stream()
            .filter(cat -> cat.getTitle().toLowerCase().contains(keyword))
            .collect(Collectors.toList());
        tabelcategory.setItems(FXCollections.observableArrayList(categories));
    }

    @FXML
    private void handleExitCategory(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setupRowClickForUpdate() {
        tabelcategory.setRowFactory(tv -> {
            TableRow<Category> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Category category = row.getItem();
                    addField.setText(category.getTitle());
                }
            });
            return row;
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

