package com.example.demo2.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://mesqd1.mysql.database.azure.com/";
    final String DB_URL = MYSQL_SERVER_URL + "csc311capstonedb";
    final String USERNAME = "mesqd";
    final String PASSWORD = "csc311DB25";

    public void insertUser(String username, String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(
                     "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                System.out.println("A new user was inserted successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int validateLoginAndGetUserId(String email, String password) {
        String query = "SELECT user_id FROM users WHERE LOWER(email) = LOWER(?) AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email.trim());
            stmt.setString(2, password.trim());

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");  // Use correct column name
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  //Login failed
    }

    public String getUsernameById(int userId) {
        String query = "SELECT username FROM users WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUsernameById(int userId, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newUsername);
            stmt.setInt(2, userId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Username updated successfully.");
            } else {
                System.out.println("No rows affected — check if user ID exists.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertProject(int userId, String projectName) {
        String sql = "INSERT INTO projects (user_id, project_name) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, projectName);
            pstmt.executeUpdate();

            System.out.println("Project inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProject(int userId, String projectName) {
        String sql = "DELETE FROM projects WHERE user_id = ? AND project_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, projectName);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Project deleted successfully.");
            } else {
                System.out.println("No matching project found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getProjectsForUser(int userId) {
        List<String> projectNames = new ArrayList<>();
        String sql = "SELECT project_name FROM projects WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                projectNames.add(rs.getString("project_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projectNames;
    }

}
