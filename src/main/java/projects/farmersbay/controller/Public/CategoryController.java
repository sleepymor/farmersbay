package projects.farmersbay.controller.Public;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static projects.farmersbay.config.Database.DB_URL;
import projects.farmersbay.model.Category;

public class CategoryController {
    public List<Category> readAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setTitle(rs.getString("title"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
