package projects.farmersbay.controller.Public;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static projects.farmersbay.config.Database.DB_URL;

public class CartController {

    public static int currentUserId = AuthController.currentUserId;

    private double getItemPrice(int itemId) throws SQLException {
        String sql = "SELECT price FROM Items WHERE ItemsID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            } else {
                throw new SQLException("Item with ID " + itemId + " not found in Items table.");
            }
        }
    }

    private int getItemStock(int itemId) throws SQLException {
        String sql = "SELECT stock FROM Items WHERE ItemsID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            } else {
                throw new SQLException("Item with ID " + itemId + " not found in Items table.");
            }
        }
    }

    private int getOrCreateUserOrderID(int userId) throws SQLException {
        String selectSql = "SELECT OrderID FROM OrderProfile WHERE UserID = ? AND OrderStatus = 'Cart'";
        String insertSql = "INSERT INTO OrderProfile (UserID, OrderStatus, OrderDate) VALUES (?, 'Cart', ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("OrderID");
                }
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, userId);
                pstmt.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                pstmt.executeUpdate();

                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating new cart (OrderProfile) failed, no OrderID obtained.");
                    }
                }
            }
        }
    }

    public void addToCart(int itemId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero to add an item.");
            return;
        }

        try {
            if (quantity > getItemStock(itemId)) {
                System.out.println("Insufficient stock for item ID: " + itemId + ". Available stock: " + getItemStock(itemId));
                return;
            }
        } catch (SQLException ex) {
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            int cartOrderId = getOrCreateUserOrderID(currentUserId);
            double itemPrice = getItemPrice(itemId);

            String checkSql = "SELECT Quantity FROM OrderItems WHERE OrderID = ? AND ItemsID = ?";
            String updateSql = "UPDATE OrderItems SET Quantity = ? WHERE OrderID = ? AND ItemsID = ?";
            String insertSql = "INSERT INTO OrderItems (OrderID, ItemsID, Quantity) VALUES (?, ?, ?)";

            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setInt(1, cartOrderId);
                checkPstmt.setInt(2, itemId);
                ResultSet rs = checkPstmt.executeQuery();

                if (rs.next()) {
                    int existingQuantity = rs.getInt("Quantity");
                    int newQuantity = existingQuantity + quantity;

                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                        updatePstmt.setInt(1, newQuantity);
                        updatePstmt.setInt(2, cartOrderId);
                        updatePstmt.setInt(3, itemId);
                        updatePstmt.executeUpdate();
                        System.out.println("Item quantity updated in cart successfully (OrderID: " + cartOrderId + ", ItemID: " + itemId + ").");
                    }
                } else {
                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                        insertPstmt.setInt(1, cartOrderId);
                        insertPstmt.setInt(2, itemId);
                        insertPstmt.setInt(3, quantity);
                        insertPstmt.executeUpdate();
                        System.out.println("Item added to cart successfully.");
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding item to cart: " + e.getMessage());
        }
    }

    public void removeFromCart(int itemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = getOrCreateUserOrderID(currentUserId);

            String sql = "DELETE FROM OrderItems WHERE OrderID = ? AND ItemsID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, cartOrderId);
                pstmt.setInt(2, itemId);
                int deleted = pstmt.executeUpdate();
                System.out.println(deleted > 0 ? "Item removed from cart." : "Item not found in cart or does not belong to your active cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error removing item from cart: " + e.getMessage());
        }
    }

    public void updateCart(int itemId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(itemId);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = getOrCreateUserOrderID(currentUserId);

            String sql = "UPDATE OrderItems SET Quantity = ? WHERE OrderID = ? AND ItemsID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, quantity);
                pstmt.setInt(2, cartOrderId);
                pstmt.setInt(3, itemId);
                int updated = pstmt.executeUpdate();
                System.out.println(updated > 0 ? "Cart updated successfully." : "Cart item not found or does not belong to your active cart.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating cart: " + e.getMessage());
        }
    }
    public void cart() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = -1;
            try {
                cartOrderId = getOrCreateUserOrderID(currentUserId);
            } catch (SQLException e) {
                System.err.println("Could not retrieve or create cart for user: " + e.getMessage());
                return;
            }

            String sql = "SELECT oi.OrderID, oi.ItemsID, i.title, oi.Quantity, i.price, i.stock "
                    + "FROM OrderItems oi "
                    + "JOIN Items i ON oi.ItemsID = i.ItemsID "
                    + "WHERE oi.OrderID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, cartOrderId);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("\n--- Your Shopping Cart ---");
                boolean hasItems = false;
                boolean hasStockIssue = false;
                while (rs.next()) {
                    hasItems = true;
                    int orderId = rs.getInt("OrderID");
                    int itemId = rs.getInt("ItemsID");
                    String itemTitle = rs.getString("title");
                    int quantity = rs.getInt("Quantity");
                    double itemPrice = rs.getDouble("price");
                    int stock = rs.getInt("stock");
                    double subtotal = quantity * itemPrice;

                    if (quantity > stock) {
                        hasStockIssue = true;
                        System.out.printf("Cart Order ID: %d, Item ID: %d, Item: %s, Quantity: %d, Price/Item: %.2f, Subtotal: %.2f [INSUFFICIENT STOCK: %d available]%n",
                                orderId, itemId, itemTitle, quantity, itemPrice, subtotal, stock);
                    } else {
                        System.out.printf("Cart Order ID: %d, Item ID: %d, Item: %s, Quantity: %d, Price/Item: %.2f, Subtotal: %.2f%n",
                                orderId, itemId, itemTitle, quantity, itemPrice, subtotal);
                    }
                }
                if (!hasItems) {
                    System.out.println("Your cart is empty.");
                } else if (hasStockIssue) {
                    System.out.println("WARNING: Some items in your cart have insufficient stock. Please update your cart.");
                }
                System.out.println("--------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error viewing cart: " + e.getMessage());
        }
    }

    // Modified checkout to reduce stock and update order status to 'Complete'
    public int checkout() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false);

            int cartOrderId = -1;
            try {
                cartOrderId = getOrCreateUserOrderID(currentUserId);
            } catch (SQLException e) {
                System.err.println("Could not retrieve cart for checkout: " + e.getMessage());
                if (conn != null) {
                    conn.rollback();
                }
                return -1;
            }

            String checkCartItemsSql = "SELECT COUNT(*) FROM OrderItems WHERE OrderID = ?";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkCartItemsSql)) {
                checkPstmt.setInt(1, cartOrderId);
                ResultSet rs = checkPstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Your cart is empty. Nothing to checkout.");
                    if (conn != null) {
                        conn.rollback();
                    }
                    return -1;
                }
            }

            // Validate stock for all items in cart
            String cartItemsSql = "SELECT oi.ItemsID, oi.Quantity, i.stock FROM OrderItems oi JOIN Items i ON oi.ItemsID = i.ItemsID WHERE oi.OrderID = ?";
            try (PreparedStatement cartItemsPstmt = conn.prepareStatement(cartItemsSql)) {
                cartItemsPstmt.setInt(1, cartOrderId);
                ResultSet rs = cartItemsPstmt.executeQuery();
                while (rs.next()) {
                    int itemId = rs.getInt("ItemsID");
                    int quantity = rs.getInt("Quantity");
                    int stock = rs.getInt("stock");
                    if (quantity > stock) {
                        System.out.printf("Insufficient stock for Item ID %d. Requested: %d, Available: %d. Please update your cart.%n", itemId, quantity, stock);
                        if (conn != null) {
                            conn.rollback();
                        }
                        return -1;
                    }
                }
            }

            // Reduce stock for each item
            String updateStockSql = "UPDATE Items SET stock = stock - ? WHERE ItemsID = ?";
            try (PreparedStatement updateStockPstmt = conn.prepareStatement(updateStockSql)) {
                String getCartItemsSql = "SELECT ItemsID, Quantity FROM OrderItems WHERE OrderID = ?";
                try (PreparedStatement getCartItemsPstmt = conn.prepareStatement(getCartItemsSql)) {
                    getCartItemsPstmt.setInt(1, cartOrderId);
                    ResultSet rs = getCartItemsPstmt.executeQuery();
                    while (rs.next()) {
                        int itemId = rs.getInt("ItemsID");
                        int quantity = rs.getInt("Quantity");
                        updateStockPstmt.setInt(1, quantity);
                        updateStockPstmt.setInt(2, itemId);
                        updateStockPstmt.addBatch();
                    }
                    updateStockPstmt.executeBatch();
                }
            }

            // Update order status to 'Complete'
            String updateOrderProfileSql = "UPDATE OrderProfile SET OrderStatus = 'Complete', OrderDate = ? WHERE OrderID = ? AND UserID = ? AND OrderStatus = 'Cart'";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateOrderProfileSql)) {
                updatePstmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                updatePstmt.setInt(2, cartOrderId);
                updatePstmt.setInt(3, currentUserId);
                int updatedRows = updatePstmt.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("Failed to update order status. Cart not found or not in 'Cart' status for this user.");
                }
                System.out.println("Order status updated to 'Complete' for Order ID: " + cartOrderId);
            }

            conn.commit();
            System.out.println("Checkout successful! Order ID: " + cartOrderId);
            return cartOrderId;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error during checkout: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
