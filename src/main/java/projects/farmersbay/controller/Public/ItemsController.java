package projects.farmersbay.controller.Public;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static projects.farmersbay.config.Database.DB_URL;
import projects.farmersbay.model.Items;


public class ItemsController {
    public List<Items> readAll() {
        List<Items> itemsList = new ArrayList<>();
        String sql = "SELECT i.*, c.CategoryName " +
                      "FROM Items i " +
                    "LEFT JOIN Category c ON i.CategoryID = c.CategoryID";

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Items item = new Items();
                item.setItemId(rs.getInt("ItemsID"));
                item.setTitle(rs.getString("title"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setImg(rs.getString("img"));
                item.setAdminId(rs.getInt("AdminID"));
                item.setDescription(rs.getString("description"));
                item.setCategoryName(rs.getString("CategoryName"));
                itemsList.add(item);
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsList;
    }
}
