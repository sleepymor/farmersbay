package projects.farmersbay.view.Public;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
    
    @FXML
    private Pane product;

    @FXML
    private TextField search;

    @FXML
    private Text Logo;

    @FXML
    private Cart C;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    displayCategories();
    displayProduct();
    search.textProperty().addListener((observable, oldValue, newValue) -> {
        displayProductFiltered(newValue);
    });
    }
    
    private void displayProduct() {
    List<Items> items = itemController.readAll();
    System.out.println("Jumlah produk ditemukan: " + items.size());
    
    Logo.setOnMouseClicked(event -> {
        displayProduct();
        search.clear();
    });

    product.getChildren().clear();

    if (items.isEmpty()) {
        System.out.println("Belum ada produk.");
        return;
    }

    int itemsPerRow = 5;
    double paneWidth = 139;
    double paneHeight = 257;
    double hGap = 30;
    double vGap = 30;

    double startX = (product.getPrefWidth() - (itemsPerRow * paneWidth + (itemsPerRow - 1) * hGap)) / 2;
    double x = startX;
    double y = 20;
    int col = 0;

    for (Items item : items) {
        Pane pane = new Pane();
        pane.setPrefSize(paneWidth, paneHeight);
        pane.setStyle("-fx-background-color: rgb(255, 255, 255);");

        ImageView imageView = new ImageView();
    
        try {
            if (item.getImg() != null && !item.getImg().isEmpty()) {
                String imagePath = "file:" + item.getImg();
                System.out.println("Trying to load image from: " + imagePath);
                Image image = new Image(imagePath, false);
                imageView.setImage(image);
            } else {
                System.out.println("Image path kosong atau null untuk item: " + item.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setLayoutX(14);
        imageView.setLayoutY(14);

        Label title = new Label(item.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setPrefWidth(110);
        title.setLayoutX(14);
        title.setLayoutY(148);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setOnMouseClicked(event -> openProductDetail(item));

        // Stok
        Label stock = new Label("Stok: " + item.getStock());
        stock.setFont(Font.font("System", FontWeight.NORMAL, 12));
        stock.setPrefWidth(110);
        stock.setLayoutX(14);
        stock.setLayoutY(168);
        stock.setAlignment(Pos.CENTER);
        stock.setTextAlignment(TextAlignment.CENTER);

        // Harga
        Label price = new Label("Rp. " + item.getPrice());
        price.setFont(Font.font("System", FontWeight.BOLD, 16));
        price.setPrefWidth(110);
        price.setLayoutX(14);
        price.setLayoutY(187);
        price.setAlignment(Pos.CENTER);
        price.setTextAlignment(TextAlignment.CENTER);

        // Tombol Add to Cart
        Button addToCart = new Button("Add to Cart");
        addToCart.setFont(Font.font("System", FontWeight.BOLD, 12));
        addToCart.setPrefSize(88, 23);
        addToCart.setLayoutX(26);
        addToCart.setLayoutY(215);
        addToCart.setStyle("-fx-background-color: #00bc01; -fx-text-fill: white;");
        addToCart.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(imageView,title, stock, price, addToCart);
        pane.setEffect(new DropShadow());

        pane.setLayoutX(x);
        pane.setLayoutY(y);
        product.getChildren().add(pane);

        col++;
        if (col == itemsPerRow) {
            col = 0;
            x = startX;
            y += paneHeight + vGap;
        } else {
            x += paneWidth + hGap;
        }
    }

    int totalRows = (int) Math.ceil((double) items.size() / itemsPerRow);
    double totalHeight = y + paneHeight + vGap;
    product.setPrefHeight(totalHeight);
}

    private void openProductDetail(Items item) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/productpage.fxml"));
        Parent root = loader.load();

        Detail controller = loader.getController();
        controller.setProductData(item);

        // Ambil stage dari node yang aktif saat ini
        Stage stage = (Stage) product.getScene().getWindow(); // Ganti "product" jika perlu referensi lain
        stage.setScene(new Scene(root));
        stage.setTitle("Detail Produk");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
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
    
    private void displayProductFiltered(String keyword) {
    if (keyword == null || keyword.trim().isEmpty()) {
        displayProduct();
        return;
    }

    List<Items> items = itemController.readAll().stream()
        .filter(item -> item.getTitle().toLowerCase().contains(keyword.toLowerCase()))
        .collect(Collectors.toList());

    product.getChildren().clear();

    if (items.isEmpty()) {
        System.out.println("Tidak ditemukan produk dengan keyword: " + keyword);
        return;
    }

    int itemsPerRow = 5;
    double paneWidth = 139;
    double paneHeight = 257;
    double hGap = 30;
    double vGap = 30;

    double startX = (product.getPrefWidth() - (itemsPerRow * paneWidth + (itemsPerRow - 1) * hGap)) / 2;
    double x = startX;
    double y = 20;
    int col = 0;

    for (Items item : items) {
        Pane pane = new Pane();
        pane.setPrefSize(paneWidth, paneHeight);
        pane.setStyle("-fx-background-color: rgb(255, 255, 255);");

        ImageView imageView = new ImageView();
        try {
            if (item.getImg() != null && !item.getImg().isEmpty()) {
                Image image = new Image("file:" + item.getImg());
                imageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setLayoutX(14);
        imageView.setLayoutY(14);

        Label title = new Label(item.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setPrefWidth(110);
        title.setLayoutX(14);
        title.setLayoutY(148);
        title.setAlignment(Pos.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);

        Label stock = new Label("Stok: " + item.getStock());
        stock.setFont(Font.font("System", FontWeight.NORMAL, 12));
        stock.setPrefWidth(110);
        stock.setLayoutX(14);
        stock.setLayoutY(168);
        stock.setAlignment(Pos.CENTER);
        stock.setTextAlignment(TextAlignment.CENTER);

        Label price = new Label("Rp. " + item.getPrice());
        price.setFont(Font.font("System", FontWeight.BOLD, 16));
        price.setPrefWidth(110);
        price.setLayoutX(14);
        price.setLayoutY(187);
        price.setAlignment(Pos.CENTER);
        price.setTextAlignment(TextAlignment.CENTER);

        Button addToCart = new Button("Add to Cart");
        addToCart.setFont(Font.font("System", FontWeight.BOLD, 12));
        addToCart.setPrefSize(88, 23);
        addToCart.setLayoutX(26);
        addToCart.setLayoutY(215);
        addToCart.setStyle("-fx-background-color: #00bc01; -fx-text-fill: white;");
        addToCart.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(imageView, title, stock, price, addToCart);
        pane.setEffect(new DropShadow());

        pane.setLayoutX(x);
        pane.setLayoutY(y);
        product.getChildren().add(pane);

        col++;
        if (col == itemsPerRow) {
            col = 0;
            x = startX;
            y += paneHeight + vGap;
        } else {
            x += paneWidth + hGap;
        }
    }

    int totalRows = (int) Math.ceil((double) items.size() / itemsPerRow);
    double totalHeight = y + paneHeight + vGap;
    product.setPrefHeight(totalHeight);
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

