package projects.farmersbay.controller.Public;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; // For formatting date

import static projects.farmersbay.config.Database.DB_URL;

public class CartController {
    // IMPORTANT: Using a static currentUserId is highly discouraged for multi-user applications.
    // In a real web application, userId should be managed via sessions or security contexts.
    public static int currentUserId = AuthController.currentUserId;

    /**
     * Helper method to get the current price of an item from the Items table.
     * Assumes 'price' column in Items is INTEGER as per your schema.
     * @param itemId The ID of the item.
     * @return The price of the item as a double for calculations.
     * @throws SQLException If the item is not found or a DB error occurs.
     */
    private double getItemPrice(int itemId) throws SQLException {
        String sql = "SELECT price FROM Items WHERE ItemsID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price"); // Still read as double for calculations, even if DB is INTEGER
            } else {
                throw new SQLException("Item with ID " + itemId + " not found in Items table.");
            }
        }
    }

    /**
     * Helper to get the current user's active cart OrderID (OrderProfile with status 'Cart').
     * If no such cart exists, it creates one.
     * @param userId The ID of the current logged-in user.
     * @return The OrderID of the user's active cart.
     * @throws SQLException If a DB error occurs during lookup or creation.
     */
    private int getOrCreateUserOrderID(int userId) throws SQLException {
        String selectSql = "SELECT OrderID FROM OrderProfile WHERE UserID = ? AND OrderStatus = 'Cart'";
        // OrderDate is DATE type, so format to 'yyyy-MM-dd'
        String insertSql = "INSERT INTO OrderProfile (UserID, OrderStatus, OrderDate) VALUES (?, 'Cart', ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // 1. Try to find an existing cart (OrderProfile with 'Cart' status) for the user
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("OrderID");
                }
            }

            // 2. If no active cart exists, create a new OrderProfile for the cart
            try (PreparedStatement pstmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, userId);
                // Format date to 'yyyy-MM-dd' for DATE type column
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

    /**
     * Adds an item to the user's shopping cart.
     * If the item already exists in the cart, its quantity is updated.
     * Follows schema strictly: OrderItems has (OrderID, ItemsID, Quantity) - no OrderItemID, no PriceAtPurchase.
     *
     * @param itemId   The ID of the item to add.
     * @param quantity The quantity of the item to add.
     */
    public void addToCart(int itemId, int quantity) {
        if (quantity <= 0) {
            System.out.println("Quantity must be greater than zero to add an item.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false); // Start transaction

            int cartOrderId = getOrCreateUserOrderID(currentUserId); // Get or create the OrderID for the cart
            double itemPrice = getItemPrice(itemId); // Get current price from Items table (for display, not storage)

            // Check if the item already exists in this specific cart (cartOrderId)
            // No OrderItemID column, so we identify an item by (OrderID, ItemsID)
            String checkSql = "SELECT Quantity FROM OrderItems WHERE OrderID = ? AND ItemsID = ?";
            // UPDATE: Only update Quantity as PriceAtPurchase does not exist in schema
            String updateSql = "UPDATE OrderItems SET Quantity = ? WHERE OrderID = ? AND ItemsID = ?";
            // INSERT: Only insert OrderID, ItemsID, Quantity as PriceAtPurchase does not exist
            String insertSql = "INSERT INTO OrderItems (OrderID, ItemsID, Quantity) VALUES (?, ?, ?)";

            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql)) {
                checkPstmt.setInt(1, cartOrderId);
                checkPstmt.setInt(2, itemId);
                ResultSet rs = checkPstmt.executeQuery();

                if (rs.next()) {
                    // Item found, update its quantity
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
                    // Item not found, insert as a new cart item
                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                        insertPstmt.setInt(1, cartOrderId);
                        insertPstmt.setInt(2, itemId);
                        insertPstmt.setInt(3, quantity);
                        insertPstmt.executeUpdate();
                        System.out.println("Item added to cart successfully.");
                    }
                }
            }
            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error adding item to cart: " + e.getMessage());
        }
    }

    /**
     * Removes an item from the user's shopping cart.
     * IMPORTANT: This method now takes 'itemId' because there's no 'OrderItemID' (CartID) in your schema.
     * It removes the item matching the given itemId from the user's active cart.
     *
     * @param itemId The ID of the item to remove.
     */
    public void removeFromCart(int itemId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = getOrCreateUserOrderID(currentUserId); // Get the cart OrderID

            // SQL to delete from OrderItems where OrderID matches the cart and ItemsID matches
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

    /**
     * Updates the quantity of an item in the user's shopping cart.
     * IMPORTANT: This method now takes 'itemId' because there's no 'OrderItemID' (CartID) in your schema.
     * If quantity is 0 or less, the item is removed.
     *
     * @param itemId The ID of the item to update.
     * @param quantity The new quantity for the item.
     */
    public void updateCart(int itemId, int quantity) {
        if (quantity <= 0) {
            removeFromCart(itemId); // Remove if quantity is 0 or less
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = getOrCreateUserOrderID(currentUserId); // Get the cart OrderID

            // SQL to update OrderItems where OrderID matches the cart and ItemsID matches
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

    /**
     * Displays all items currently in the user's shopping cart.
     * The output will show OrderID and ItemsID as identifiers, as there's no specific OrderItemID column.
     */
    public void cart() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int cartOrderId = -1;
            try {
                cartOrderId = getOrCreateUserOrderID(currentUserId);
            } catch (SQLException e) {
                System.err.println("Could not retrieve or create cart for user: " + e.getMessage());
                return;
            }

            // Select all items from OrderItems for the active cart's OrderID
            // Join with Items to get item title and price.
            String sql = "SELECT oi.OrderID, oi.ItemsID, i.title, oi.Quantity, i.price " +
                         "FROM OrderItems oi " +
                         "JOIN Items i ON oi.ItemsID = i.ItemsID " +
                         "WHERE oi.OrderID = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, cartOrderId);
                ResultSet rs = pstmt.executeQuery();

                System.out.println("\n--- Your Shopping Cart ---");
                boolean hasItems = false;
                while (rs.next()) {
                    hasItems = true;
                    int orderId = rs.getInt("OrderID"); // This is the Cart's OrderID
                    int itemId = rs.getInt("ItemsID");
                    String itemTitle = rs.getString("title");
                    int quantity = rs.getInt("Quantity");
                    double itemPrice = rs.getDouble("price"); // Get current price from Items table
                    double subtotal = quantity * itemPrice;

                    // Display OrderID and ItemsID to identify the cart item line
                    System.out.printf("Cart Order ID: %d, Item ID: %d, Item: %s, Quantity: %d, Price/Item: %.2f, Subtotal: %.2f%n",
                                      orderId, itemId, itemTitle, quantity, itemPrice, subtotal);
                }
                if (!hasItems) {
                    System.out.println("Your cart is empty.");
                }
                System.out.println("--------------------------\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error viewing cart: " + e.getMessage());
        }
    }

    /**
     * Processes the checkout for the user's cart.
     * This involves changing the OrderStatus of the active cart's OrderProfile
     * from 'Cart' to a 'Pending' or 'Completed' status.
     * @return The OrderID of the finalized order, or -1 if checkout fails.
     */
    public int checkout() {
        Connection conn = null; // Declare conn outside try-with-resources for rollback in catch
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(false); // Start transaction for checkout

            int cartOrderId = -1;
            try {
                cartOrderId = getOrCreateUserOrderID(currentUserId); // Get the cart OrderID
            } catch (SQLException e) {
                System.err.println("Could not retrieve cart for checkout: " + e.getMessage());
                if (conn != null) conn.rollback(); // Rollback on error
                return -1;
            }

            // 1. Check if the user's cart is empty
            String checkCartItemsSql = "SELECT COUNT(*) FROM OrderItems WHERE OrderID = ?";
            try(PreparedStatement checkPstmt = conn.prepareStatement(checkCartItemsSql)) {
                checkPstmt.setInt(1, cartOrderId);
                ResultSet rs = checkPstmt.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    System.out.println("Your cart is empty. Nothing to checkout.");
                    if (conn != null) conn.rollback(); // Rollback if cart is empty
                    return -1;
                }
            }

            // 2. Update the OrderStatus of the OrderProfile from 'Cart' to 'Pending' (or 'Processing')
            // Also update OrderDate to reflect checkout time (formatted for DATE column)
            String updateOrderProfileSql = "UPDATE OrderProfile SET OrderStatus = 'Pending', OrderDate = ? WHERE OrderID = ? AND UserID = ? AND OrderStatus = 'Cart'";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateOrderProfileSql)) {
                // Format date to 'yyyy-MM-dd' for DATE type column
                updatePstmt.setString(1, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                updatePstmt.setInt(2, cartOrderId);
                updatePstmt.setInt(3, currentUserId);
                int updatedRows = updatePstmt.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("Failed to update order status. Cart not found or not in 'Cart' status for this user.");
                }
                System.out.println("Order status updated for Order ID: " + cartOrderId);
            }

            // 3. (Optional but recommended) Update prices in OrderItems to current prices from Items table at checkout.
            // THIS ASSUMES you *could* add a PriceAtPurchase column to OrderItems.
            // However, based on your current schema image, there is NO PriceAtPurchase column.
            // So, this operation cannot be performed. The prices printed in cart() will be current Item prices,
            // but the historical order items will only have Quantity and will implicitly rely on Items.price
            // at the time of retrieval, which is not ideal for historical accuracy.
            // I'm commenting out the SQL that would update price as it doesn't exist in your schema.
            /*
            String updatePricesSql = "UPDATE OrderItems " +
                                     "SET PriceAtPurchase = (SELECT price FROM Items WHERE Items.ItemsID = OrderItems.ItemsID) " +
                                     "WHERE OrderID = ?";
            try (PreparedStatement updatePricesPstmt = conn.prepareStatement(updatePricesSql)) {
                updatePricesPstmt.setInt(1, cartOrderId);
                updatePricesPstmt.executeUpdate();
                System.out.println("Prices updated for items in order " + cartOrderId);
            }
            */
            System.out.println("Note: PriceAtPurchase column is not in OrderItems schema, so prices are not explicitly stored with the order items.");


            conn.commit(); // Commit transaction
            System.out.println("Checkout successful! Order ID: " + cartOrderId);
            return cartOrderId;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error during checkout: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.close(); // Close connection in finally block
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}