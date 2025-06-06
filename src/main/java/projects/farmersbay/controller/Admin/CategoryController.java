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
import projects.farmersbay.model.Category;

public class CategoryController extends Controller<Category> {

    @Override
    public void create(Category category) {
        String sql = "INSERT INTO Category (title) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, category.getTitle());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                category.setCategoryID(generatedKeys.getInt(1));
                System.out.println("Category created with ID: " + category.getCategoryID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Category category) {
        String sql = "UPDATE Category SET title = ? WHERE CategoryID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category.getTitle());
            pstmt.setInt(2, category.getCategoryID());

            int updated = pstmt.executeUpdate();
            System.out.println(updated > 0 ? "Category updated: " + category.getTitle() : "Category not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Category WHERE CategoryID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int deleted = pstmt.executeUpdate();
            System.out.println(deleted > 0 ? "Category deleted with ID: " + id : "Category not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Category read(Integer id) {
        String sql = "SELECT * FROM Category WHERE CategoryID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Category(rs.getInt("CategoryID"), rs.getString("Title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Category> readAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category";

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(new Category(rs.getInt("CategoryID"), rs.getString("Title")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}
