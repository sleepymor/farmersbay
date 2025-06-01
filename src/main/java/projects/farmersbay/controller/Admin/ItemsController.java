package projects.farmersbay.controller.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static projects.farmersbay.config.Database.DB_URL;
import projects.farmersbay.controller.Controller;
import projects.farmersbay.model.Items;

public class ItemsController extends Controller<Items> {

    @Override
    public void create(Items item) {
        String sql = "INSERT INTO Items (itemName, price, stock, AdminID) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getStock());
            pstmt.setInt(4, item.getAdminId());

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
        String sql = "UPDATE Items SET itemName = ?, price = ?, stock = ?, AdminID = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getStock());
            pstmt.setInt(4, item.getAdminId());
            pstmt.setInt(5, item.getItemId());

            int updated = pstmt.executeUpdate();
            System.out.println(updated > 0 ? "Item updated: " + item.getItemName() : "Item not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Items WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int deleted = pstmt.executeUpdate();
            System.out.println(deleted > 0 ? "Item deleted with ID: " + id : "Item not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Items read(Integer id) {
        String sql = "SELECT * FROM Items WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Items item = new Items();
                item.setItemId(rs.getInt("id"));
                item.setItemName(rs.getString("itemName"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setAdminId(rs.getInt("AdminID"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Item not found with ID: " + id);
        return null;
    }

    public List<Items> readAll() {
        List<Items> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM Items";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Items item = new Items();
                item.setItemId(rs.getInt("id"));
                item.setItemName(rs.getString("itemName"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setAdminId(rs.getInt("AdminID"));
                itemsList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemsList;
    }
}
