package projects.farmersbay.view.Public;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import projects.farmersbay.controller.Admin.CategoryController;
import projects.farmersbay.controller.Public.ItemsController;
import projects.farmersbay.model.Category;
import projects.farmersbay.model.Items;

public class Home implements Initializable {

    @FXML
    private Pane categoryPane;
    private final CategoryController categoryController = new CategoryController();

    private final ItemsController itemController = new ItemsController();
    private List<Pane> productPanes;

    @FXML private Pane product1;
    @FXML private Pane product2;
    @FXML private Pane product3;
    @FXML private Pane product4;
    @FXML private Pane product5;
    @FXML private Pane product6;
    @FXML private Pane product7;
    @FXML private Pane product8;
    @FXML private Pane product9;
    @FXML private Pane product10;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    displayCategories();
    productPanes = List.of(product1, product2, product3, product4, product5,
                           product6, product7, product8, product9, product10);
    displayProducts();
    }

    private void displayProducts() {
    List<Items> items = itemController.readAll();
    System.out.println("Jumlah produk ditemukan: " + items.size()); 
    for (int i = 0; i < Math.min(items.size(), productPanes.size()); i++) {
        Pane pane = productPanes.get(i);
        Items item = items.get(i);

        pane.getChildren().clear();

        // Gambar produk
        String imagePath = item.getImg();
        Image image;
        ImageView imageView;

        if (imagePath == null || imagePath.isEmpty() || !(new File(imagePath).exists())) {
            System.out.println("Gambar tidak ditemukan: " + imagePath + ", gambar tidak akan ditampilkan.");
            imageView = new ImageView(); // kosong, tidak tampil gambar
        } else {
            image = new Image(new File(imagePath).toURI().toString(), 60, 60, true, true);
            imageView = new ImageView(image);
    imageView.setFitHeight(100);
    imageView.setFitWidth(100);
    imageView.setLayoutX(70);
    imageView.setLayoutY(70);
    pane.getChildren().add(imageView); // tambahkan hanya jika gambar valid
}
        // Nama Produk
        Label title = new Label(item.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setLayoutX(53);
        title.setLayoutY(145);

        // Stok
        Label stock = new Label("Stok: " + item.getStock());
        stock.setFont(Font.font("System", 12));
        stock.setLayoutX(47);
        stock.setLayoutY(69);

        // Harga
        Label price = new Label("Rp. " + item.getPrice());
        price.setFont(Font.font("System", FontWeight.BOLD, 16));
        price.setLayoutX(25);
        price.setLayoutY(196);

        // Tombol "Add to Cart"
        Button addToCart = new Button("Add to cart");
        addToCart.setPrefSize(104, 25);
        addToCart.setStyle("-fx-background-color: #d3d3d3; -fx-text-fill: black;");
        addToCart.setLayoutX(17);
        addToCart.setLayoutY(212);

        // Tambahkan ke Pane
        pane.getChildren().addAll(title, stock, price, addToCart);
    }
}

    public void displayCategories() {
    categoryPane.getChildren().removeIf(node ->
        node instanceof Label && "category-label".equals(node.getStyleClass().stream().findFirst().orElse(null))
    );

    List<Category> categories = categoryController.readAll();
    int maxCategories = Math.min(categories.size(), 8);

    double startX = 33;
    double startY = 39;
    double xSpacing = 172;
    double ySpacing = 32;

    for (int i = 0; i < maxCategories; i++) {
            Category category = categories.get(i);

            Label label = new Label(category.getTitle());
            label.setFont(Font.font("System", FontWeight.BOLD, 14));
            label.setTextFill(Color.WHITE);
    
    int col = i % 4;
    int row = i / 4;

    double layoutX = startX + (col * xSpacing);
    double layoutY = startY + (row * ySpacing);

        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);

        categoryPane.getChildren().add(label);

        System.out.println("Menjalankan displayCategories()");
        System.out.println("Jumlah kategori ditemukan: " + categories.size());
        }
    }

    @FXML
    private Button User;

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

            stage.setMinWidth(320);  // ganti sesuai kebutuhan
            stage.setMinHeight(450);
            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




}

