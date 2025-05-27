package projects.farmersbay.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import projects.farmersbay.model.Items;

public class ItemController extends Controller<Items> {

    private String url = "jdbc:sqlite:src/main/resources/database/farmersbay.sqlite";

    @Override
    public void create(Items item) {
        String sql = "INSERT INTO Items (name, price, stock) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, item.getItemName());
            pstmt.setDouble(2, item.getPrice());
            pstmt.setInt(3, item.getStock());
            pstmt.executeUpdate();

            // Get the generated ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                item.setItemId(id);
                System.out.println("Item created with ID: " + id);
            } else {
                System.out.println("Item created, but ID not returned.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Items item) {
        // Implementation for updating an item
    }

    @Override
    public void delete(Integer id) {
        // Implementation for deleting an item by id
    }

    @Override
    public Items read(Integer id) {
        String sql = "SELECT * FROM Items WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Items(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"), rs.getInt("AdminID"));
            } else {
                System.out.println("Item not found with ID: " + id);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Items> readAll() {
        List<Items> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM Items";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Items item = new Items(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("stock"), rs.getInt("AdminID"));
                itemsList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsList;
    }
}
