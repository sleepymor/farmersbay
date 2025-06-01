package projects.farmersbay.controller.Public;

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
import projects.farmersbay.model.User;

public class AuthController extends Controller<User> {

    @Override
    public void create(User user) {
        String sql = "INSERT INTO User (name, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();

            // Get the generated ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                user.setId(id);
                System.out.println("User created with ID: " + id);
            } else {
                System.out.println("User created, but ID not returned.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE User SET name = ?, password = ? WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getId());
            int updated = pstmt.executeUpdate();
            System.out.println(updated > 0 ? "User updated: " + user.getName() : "User not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer UserID) {
        String sql = "DELETE FROM User WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, UserID);
            int deleted = pstmt.executeUpdate();
            System.out.println(deleted > 0 ? "User deleted with ID: " + UserID : "User not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User read(Integer UserID) {
        String sql = "SELECT * FROM User WHERE UserID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, UserID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                System.out.println("User found: " + user.getName());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User not found with ID: " + UserID);
        return null;
    }

    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public User login(String name, String password) {
        String sql = "SELECT * FROM User WHERE name = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                System.out.println("Login successful for user: " + user.getName());
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Login failed for user: " + name);
        return null;
    }
}
