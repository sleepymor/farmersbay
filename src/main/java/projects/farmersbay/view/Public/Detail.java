package projects.farmersbay.view.Public;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.util.Map;
import java.util.HashMap;
import projects.farmersbay.controller.Public.AuthController;
import projects.farmersbay.controller.Public.CartController;
import javafx.stage.Stage;
import projects.farmersbay.model.Items;
import projects.farmersbay.model.OrderItem;


public class Detail {

    @FXML
    private ImageView Image;

    @FXML
    private Text Title, Price, Stock, Desc;

    @FXML
    private Button User;

    @FXML
    private TextField QuantityField;
    private Items currentItem;

    private int currentUserId;
    private final CartController cartController = new CartController();

    public void setUserId(int userId) {
        this.currentUserId = userId;
    }

    public void setProductData(Items item) {
        this.currentItem = Cart.getItemFromCacheOrPut(item);
        Title.setText(currentItem.getTitle());
        NumberFormat formatRupiah = NumberFormat.getInstance(new Locale("id", "ID"));
        String formattedPrice = formatRupiah.format(currentItem.getPrice());
        Price.setText("Rp. " + formattedPrice);
        Stock.setText("Stok: " + currentItem.getStock());
        Desc.setText(currentItem.getDescription());

        Map<Items, OrderItem> userCart = Cart.getUserCart(currentUserId);
        if (userCart.containsKey(currentItem)) {
            int qty = userCart.get(currentItem).getQuantity();
            QuantityField.setText(String.valueOf(qty > 0 ? qty : 1));
        } else {
            QuantityField.setText("1");
        }

        if (currentItem.getImg() != null && !currentItem.getImg().isEmpty()) {
            Image.setImage(new Image(currentItem.getImg()));
        }
        QuantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int qty = Integer.parseInt(newVal.trim());
                if (qty < 1) {
                    QuantityField.setText("1");
                } else if (qty > currentItem.getStock()) {
                    QuantityField.setText(String.valueOf(currentItem.getStock()));
                    showAlert("Melebihi Stok", "Jumlah melebihi stok tersedia!");
                }
            } catch (NumberFormatException e) {
                QuantityField.setText("1");
            }
        });
    }

    @FXML
    private void handleAddToCart(MouseEvent event) {
        try {
            int qty = Integer.parseInt(QuantityField.getText().trim());
            if (qty < 1) qty = 1;
            if (qty > currentItem.getStock()) {
                showAlert("Melebihi Stok", "Jumlah melebihi stok tersedia!");
                qty = currentItem.getStock();
                QuantityField.setText(String.valueOf(qty));
            }

            cartController.addToCart(currentItem.getItemId(), qty);

            Map<Items, OrderItem> userCart = Cart.getUserCart(AuthController.currentUserId);
            if (!userCart.containsKey(currentItem)) {
                OrderItem orderItem = new OrderItem(0, currentItem.getItemId(), qty);
                userCart.put(currentItem, orderItem);
                System.out.println("Item ditambahkan ke UI Cart: " + currentItem.getTitle() + " (Qty: " + qty + ")");
            } else {
                OrderItem orderItem = userCart.get(currentItem);
                orderItem.setQuantity(orderItem.getQuantity() + qty);
                System.out.println("Quantity diperbarui di UI Cart: " + currentItem.getTitle() + " (Qty: " + orderItem.getQuantity() + ")");
            }

            showAlert("Berhasil", "Produk berhasil ditambahkan ke keranjang.");
        } catch (NumberFormatException e) {
            QuantityField.setText("1");
            showAlert("Input Salah", "Jumlah tidak valid. Telah diset ke 1.");
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
    private void handleCartClicked(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/cart.fxml"));
            Parent root = loader.load();
            Cart cartController = loader.getController();
            cartController.loadCart();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Farmers Bay");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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