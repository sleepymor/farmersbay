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
import projects.farmersbay.model.Admin; 

public class AuthController extends Controller<Admin> {
    public static int currentAdminId = -1;

    @Override
    public void create(Admin admin) {
        

        String sql = "INSERT INTO Admin (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getPassword());
            pstmt.executeUpdate();

    
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                admin.setId(id);
                System.out.println("Admin created with ID: " + id);
            } else {
                System.out.println("Admin created, but ID not returned.");
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    @Override
    public void update(Admin admin) {
        String sql = "UPDATE Admin SET username = ?, password = ? WHERE AdminID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, admin.getName());
            pstmt.setString(2, admin.getPassword());
            pstmt.setInt(3, admin.getId());
            int updated = pstmt.executeUpdate();
            System.out.println(updated > 0 ? "Admin updated: " + admin.getName() : "Admin not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer AdminID) {
        String sql = "DELETE FROM Admin WHERE AdminID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, AdminID);
            int deleted = pstmt.executeUpdate();
            System.out.println(deleted > 0 ? "Admin deleted with ID: " + AdminID : "Admin not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Admin read(Integer AdminID) {
        String sql = "SELECT * FROM Admin WHERE AdminID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, AdminID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("AdminID"));
                admin.setName(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                System.out.println("Admin found: " + admin.getName());
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Admin not found with ID: " + AdminID);
        return null;
    }

    @Override
    public List<Admin> readAll() {
        List<Admin> users = new ArrayList<>();
        String sql = "SELECT * FROM Admin";

        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("AdminID"));
                admin.setName(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                users.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public Admin login(String username, String password) {
        String sql = "SELECT * FROM Admin WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                int id = rs.getInt("AdminID");
                admin.setId(rs.getInt("AdminID"));
                admin.setName(rs.getString("username"));
                admin.setPassword(rs.getString("password"));
                AuthController.currentAdminId = id;
                System.out.println("Login successful for admin: " + admin.getName());
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Login failed for admin: " + username);
        return null;
    }
}

