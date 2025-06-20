package projects.farmersbay.view.Public;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import projects.farmersbay.controller.Public.AuthController;
import projects.farmersbay.controller.Public.CartController;
import projects.farmersbay.model.Items;
import projects.farmersbay.model.OrderItem;

public class Cart implements Initializable {

    @FXML
    private Pane cartpane;

    @FXML
    private Text Total;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView back;

    @FXML
    private Button buy;

    private final CartController cartController = new CartController();

    public static Map<Items, OrderItem> getUserCart(int userId) {
        return userCartMap.computeIfAbsent(userId, k -> new LinkedHashMap<>());
    }

    private static final Map<Integer, Map<Items, OrderItem>> userCartMap = new HashMap<>();
    private final Map<Items, Pane> itemPaneMap = new LinkedHashMap<>();

    private static final Map<Integer, Items> globalItemCache = new HashMap<>();


    public static Items getItemFromCacheOrPut(Items item) {
        if (globalItemCache.containsKey(item.getItemId())) {
            return globalItemCache.get(item.getItemId());
        }

        globalItemCache.put(item.getItemId(), item);
        return item;
    }

    private double nextY = 16;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filterCartItems(newVal.toLowerCase());
        });

        buy.disableProperty().bind(Total.textProperty().isEqualTo("Total Price : -"));
    }

    private Map<Items, OrderItem> getCurrentCart() {
        return userCartMap.computeIfAbsent(AuthController.currentUserId, k -> new HashMap<>());
    }

    public void loadCart() {
        cartpane.getChildren().clear();
        cartpane.getChildren().add(back);
        itemPaneMap.clear();
        nextY = 16;

        Map<Items, OrderItem> userCart = getCurrentCart();
        for (Map.Entry<Items, OrderItem> entry : userCart.entrySet()) {
 
            createOrUpdateCartItemPane(entry.getKey(), entry.getValue().getQuantity());
        }
        updateTotalPrice();
        realignCartItems();
    }

    private void createOrUpdateCartItemPane(Items item, int quantity) {
        Pane itemPane;
        TextField quantityField;
        Label priceLabel;

         if (itemPaneMap.containsKey(item)) {
            itemPane = itemPaneMap.get(item);
            quantityField = (TextField) itemPane.lookup("#quantityField_" + item.getItemId());
            priceLabel = (Label) itemPane.lookup("#priceLabel_" + item.getItemId());
        } else {
            itemPane = new Pane();
            itemPane.setPrefSize(524, 138);
            itemPane.setLayoutX(58);
            itemPane.setStyle(
                "-fx-border-color: #00bc01;" +
                "-fx-border-width: 2;" +
                "-fx-background-color: white;" +
                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 8, 0, 0, 2);"
            );

            ImageView image = new ImageView();
            try {
                if (item.getImg() != null && !item.getImg().isEmpty()) {
                    image.setImage(new Image(item.getImg()));
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            image.setFitWidth(130);
            image.setFitHeight(109);
            image.setLayoutX(14);
            image.setLayoutY(16);
            image.setCursor(Cursor.HAND);
            image.setOnMouseClicked(e -> openProductDetail(item));

            Label title = new Label(item.getTitle());
            title.setFont(Font.font("System", FontWeight.BOLD, 20));
            title.setLayoutX(155);
            title.setLayoutY(10);
            title.setCursor(Cursor.HAND);
            title.setOnMouseClicked(e -> openProductDetail(item));

            Label stock = new Label("Stok: " + item.getStock());
            stock.setFont(Font.font("System", FontWeight.BOLD, 11));
            stock.setLayoutX(155);
            stock.setLayoutY(32);

            quantityField = new TextField();
            quantityField.setId("quantityField_" + item.getItemId());
            quantityField.setFont(Font.font("System", 12));
            quantityField.setStyle("-fx-border-color: #00bc01;");
            quantityField.setPrefWidth(50);
            quantityField.setLayoutX(155);
            quantityField.setLayoutY(60);

            priceLabel = new Label();
            priceLabel.setId("priceLabel_" + item.getItemId());
            priceLabel.setFont(Font.font("System", FontWeight.BOLD, item.getPrice() >= 1_000_000 ? 12 : 16));
            priceLabel.setLayoutX(155);
            priceLabel.setLayoutY(92);

            Button deleteBtn = new Button("Delete");
            deleteBtn.setFont(Font.font("System", FontWeight.BOLD, 12));
            deleteBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white;");
            deleteBtn.setPrefSize(98, 19);
            deleteBtn.setLayoutX(416);
            deleteBtn.setLayoutY(48);
            deleteBtn.setCursor(Cursor.HAND);
            deleteBtn.setOnMouseClicked((MouseEvent event) -> {
                cartpane.getChildren().remove(itemPane);
                itemPaneMap.remove(item);
                getCurrentCart().remove(item); 
                realignCartItems();
                updateTotalPrice();
                showAlert("Item Dihapus", "Produk berhasil dihapus dari keranjang.");
            });

            itemPane.getChildren().addAll(image, title, stock, quantityField, priceLabel, deleteBtn);
            cartpane.getChildren().add(itemPane);
            itemPaneMap.put(item, itemPane);

            quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    int qty = Integer.parseInt(newVal.trim());
                    if (qty < 0) qty = 0;
                    if (qty > item.getStock()) {
                        qty = item.getStock();
                        quantityField.setText(String.valueOf(qty));
                        showAlert("Melebihi Stok", "Jumlah melebihi stok tersedia!");
                    }
            
                    OrderItem order = getCurrentCart().get(item);
                    if (order != null) {
                        order.setQuantity(qty);
                    } else {
                        getCurrentCart().put(item, new OrderItem(0, item.getItemId(), qty));
                    }
                    updateItemPriceLabel(item, qty, priceLabel);
                    updateTotalPrice();
                } catch (NumberFormatException e) {
                    quantityField.setText("0");
                    updateItemPriceLabel(item, 0, priceLabel);
                    updateTotalPrice();
                }
            });
        }

        quantityField.setText(String.valueOf(quantity));
        updateItemPriceLabel(item, quantity, priceLabel);
    }

    private void updateItemPriceLabel(Items item, int quantity, Label priceLabel) {
        NumberFormat formatRupiah = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedPrice = formatRupiah.format(item.getPrice() * quantity);
        priceLabel.setText("Rp. " + formattedPrice);
    }

    @FXML
    private void handlemainClick(MouseEvent event) {
        navigateToMain(event);
    }

    @FXML
    private void handleback(MouseEvent event) {
        navigateToMain(event);
    }

    private void navigateToMain(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Main.fxml"));
            Parent root = loader.load();

            Home homeController = loader.getController();
            homeController.displayProduct();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("FarmersBay");
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterCartItems(String keyword) {
        double y = 16;
        for (Map.Entry<Items, Pane> entry : itemPaneMap.entrySet()) {
            Items item = entry.getKey();
            Pane pane = entry.getValue();

            if (keyword.isEmpty() || item.getTitle().toLowerCase().contains(keyword)) {
                pane.setVisible(true);
                pane.setLayoutY(y);
                y += 138 + 4;
            } else {
                pane.setVisible(false);
            }
        }
        nextY = y;
        cartpane.setPrefHeight(nextY + 30);
    }

    private void realignCartItems() {
        double y = 16;
        for (Pane pane : itemPaneMap.values()) {
            if (pane.isVisible()) {
                pane.setLayoutY(y);
                y += 138 + 4;
            }
        }
        nextY = y;
        cartpane.setPrefHeight(nextY + 30);
    }

    private void openProductDetail(Items item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/productpage.fxml"));
            Parent root = loader.load();

            Detail controller = loader.getController();
            controller.setProductData(item);

            Stage stage = (Stage) cartpane.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Farmers Bay");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalPrice() {
        System.out.println("Memanggil updateTotalPrice...");
        Map<Items, OrderItem> cart = getCurrentCart();
        double total = 0;

        for (Map.Entry<Items, OrderItem> entry : cart.entrySet()) {
            Items item = entry.getKey();
            OrderItem order = entry.getValue();
            int quantity = order.getQuantity();
            double price = item.getPrice();

            System.out.println("Item: " + item.getTitle() + ", Qty: " + quantity + ", Harga: " + price);

            total += price * quantity;
        }

        if (cart.isEmpty()) {
            Total.setText("Total Price : -");
        } else {
            NumberFormat formatRupiah = NumberFormat.getInstance(new Locale("id", "ID"));
            String formatted = formatRupiah.format(total);
            Total.setText("Total Price : Rp. " + formatted);
            System.out.println("Total dihitung: " + total);
        }
    }

    @FXML
    private Button User;

    @FXML
    private void handleuserClick(MouseEvent event) {
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


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void handleBuyClicked(MouseEvent event) {
        if (getCurrentCart().isEmpty()) {
            showAlert("Keranjang Kosong", "Tidak ada item di keranjang untuk dibeli.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi Pembelian");
        confirmAlert.setHeaderText("Apakah Anda yakin ingin membeli item-item ini?");
        confirmAlert.setContentText("Klik OK untuk melanjutkan pembayaran.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Map<Items, OrderItem> itemsToCheckout = new HashMap<>(getCurrentCart());
            int orderId = cartController.checkout();
            if (orderId != -1) {
                showAlert("Pembelian Berhasil", "Pesanan Anda (ID: " + orderId + ") telah berhasil diproses!");
                for (Map.Entry<Items, OrderItem> entry : itemsToCheckout.entrySet()) {
                Items purchasedItem = entry.getKey();
                int purchasedQuantity = entry.getValue().getQuantity();
                int newStock = purchasedItem.getStock() - purchasedQuantity;
                purchasedItem.setStock(newStock);
            }
                getCurrentCart().clear();
                loadCart();


                navigateToMain(event);

            } else {
                showAlert("Pembelian Gagal", "Terjadi masalah saat memproses pesanan Anda. Mohon periksa kembali stok atau coba lagi.");
                loadCart();
            }
        }
    }
}