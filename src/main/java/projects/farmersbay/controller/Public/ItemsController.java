package projects.farmersbay.controller.Public;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static projects.farmersbay.config.Database.DB_URL;
import projects.farmersbay.model.Items;



public class ItemsController {
    public List<Items> readAll() {
        List<Items> itemsList = new ArrayList<>();
        String sql = "SELECT * FROM Items";

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Items item = new Items();
                item.setItemId(rs.getInt("ItemsID"));
                item.setTitle(rs.getString("title"));
                item.setPrice(rs.getDouble("price"));
                item.setStock(rs.getInt("stock"));
                item.setImg(rs.getString("img"));
                item.setAdminId(rs.getInt("AdminID"));
                itemsList.add(item);
            
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemsList;
    }
}
