package projects.farmersbay.controller.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static projects.farmersbay.config.Database.DB_URL;
import projects.farmersbay.controller.Controller;
import projects.farmersbay.model.Items;

public class ItemsController extends Controller<Items> {

    @Override
    public void create(Items item) {
        String sql = "INSERT INTO Items (title, price, stock, AdminID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getTitle());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getStock());

            // Automatically assign current admin ID
            pstmt.setInt(4, AuthController.currentAdminId);

            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                item.setItemId(generatedKeys.getInt(1));
                System.out.println("Item created with ID: " + item.getItemId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Items item) {
        String sql = "UPDATE Items SET title = ?, price = ?, stock = ?, AdminID = ? WHERE ItemsID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getTitle());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getStock());

            // Automatically update to current admin ID
            pstmt.setInt(4, AuthController.currentAdminId);
            pstmt.setInt(5, item.getItemId());

            int updated = pstmt.executeUpdate();
            System.out.println(updated > 0 ? "Item updated: " + item.getTitle() : "Item not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer ItemsID) {
        String sql = "DELETE FROM Items WHERE ItemsID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ItemsID);
            int deleted = pstmt.executeUpdate();
            System.out.println(deleted > 0 ? "Item deleted with ID: " + ItemsID : "Item not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Items read(Integer ItemsID) {
        String sql = "SELECT * FROM Items WHERE ItemsID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ItemsID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Items item = new Items();
                item.setItemId(rs.getInt("ItemsID"));
                item.setTitle(rs.getString("title"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setAdminId(rs.getInt("AdminID"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Item not found with ID: " + ItemsID);
        return null;
    }

    @Override
    public List<Items> readAll() {
        List<Items> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM Items WHERE AdminID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, AuthController.currentAdminId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Items item = new Items();
                    item.setItemId(rs.getInt("ItemsID"));
                    item.setTitle(rs.getString("title"));
                    item.setPrice(rs.getDouble("price"));
                    item.setStock(rs.getInt("stock"));
                    item.setAdminId(rs.getInt("AdminID"));
                    itemsList.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // consider using a logger in production
        }

        return itemsList;
    }

}
