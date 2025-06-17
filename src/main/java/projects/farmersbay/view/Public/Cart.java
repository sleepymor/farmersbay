package projects.farmersbay.view.Public;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
import javafx.scene.Cursor;

import java.util.ArrayList;
import java.util.List;
import projects.farmersbay.controller.Public.CartController;
import projects.farmersbay.model.Items;
import projects.farmersbay.controller.Public.AuthController; 

public class Cart {

        @FXML
    private Pane cartpane; // fx:id dari FXML

    private List<Integer> cartItemIDs = new ArrayList<>(); // untuk mencegah duplikasi

    private double nextY = 16; // posisi Y awal

    public void addItemToCart(Items item) {
        if (cartItemIDs.contains(item.getItemsID())) return; // skip jika sudah ditambahkan
        cartItemIDs.add(item.getItemsID());

        Pane itemPane = new Pane();
        itemPane.setPrefSize(524, 138);
        itemPane.setLayoutX(58);
        itemPane.setLayoutY(nextY);

        // Image produk
        ImageView image = new ImageView();
        try {
            image.setImage(new Image("file:" + item.getImg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        image.setFitWidth(130);
        image.setFitHeight(109);
        image.setLayoutX(14);
        image.setLayoutY(16);

        // Label title
        Label title = new Label(item.getTitle());
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setLayoutX(155);
        title.setLayoutY(36);

        // Label stock
        Label stock = new Label("Stok: " + item.getStock());
        stock.setFont(Font.font("System", FontWeight.BOLD, 11));
        stock.setLayoutX(155);
        stock.setLayoutY(48);

        // TextField quantity
        TextField quantityField = new TextField("1");
        quantityField.setFont(Font.font("System", 12));
        quantityField.setStyle("-fx-border-color: #00bc01;");
        quantityField.setPrefWidth(50);
        quantityField.setLayoutX(155);
        quantityField.setLayoutY(60);

        // Label harga total
        Label priceLabel = new Label("Rp. " + item.getPrice());
        priceLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        priceLabel.setLayoutX(155);
        priceLabel.setLayoutY(113);

        
        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                int qty = Integer.parseInt(newVal);
                int total = item.getPrice() * qty;
                priceLabel.setText("Rp. " + total);
            } catch (NumberFormatException e) {
                priceLabel.setText("Rp. 0");
            }
        });

        // Tombol delete (icon)
        ImageView deleteBtn = new ImageView(new Image("@icon/2.png"));
        deleteBtn.setFitWidth(56);
        deleteBtn.setFitHeight(43);
        deleteBtn.setLayoutX(456);
        deleteBtn.setLayoutY(48);
        deleteBtn.setCursor(Cursor.HAND);

        deleteBtn.setOnMouseClicked(event -> {
            cartpane.getChildren().remove(itemPane);
            cartItemIDs.remove((Integer) item.getItemsID());
            realignCartItems();
        });

        itemPane.getChildren().addAll(image, title, stock, quantityField, priceLabel, deleteBtn);
        cartpane.getChildren().add(itemPane);

        nextY += 138 + 4;
    }

    private void realignCartItems() {
    double y = 16;
    for (Node node : cartpane.getChildren()) {
        if (node instanceof Pane) {
            node.setLayoutY(y);
            y += 138 + 4;
        }
    }
    nextY = y;
}


}