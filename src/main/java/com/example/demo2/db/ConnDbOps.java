package com.example.demo2.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311mojica04.mysql.database.azure.com/";
    final String DB_URL = MYSQL_SERVER_URL + "DBname";
    final String USERNAME = "mojin";
    final String PASSWORD = "FARM123$";

    public boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        try {
            // Step 1: Create database if it doesn't exist
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            statement.close();
            conn.close();

            // Step 2: Connect to DB and create tables
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
//            createTables(statement);

            // Step 3: Check if users exist
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");
            if (resultSet.next()) {
                hasRegisteredUsers = resultSet.getInt(1) > 0;
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

//    private void createTables(Statement statement) throws SQLException {
//
//        // Projects table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS projects (" +
//                "project_id INT AUTO_INCREMENT PRIMARY KEY," +
//                "user_id INT NOT NULL," +
//                "project_name VARCHAR(255) NOT NULL," +
//                "last_accessed TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
//                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
//
//        // Components table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS components (" +
//                "component_id INT AUTO_INCREMENT PRIMARY KEY," +
//                "project_id INT NOT NULL," +
//                "component_type ENUM('Wire', 'Battery', 'Resistor', 'Switch', 'Light bulb') NOT NULL," +
//                "x_cord INT NOT NULL," +
//                "y_cord INT NOT NULL," +
//                "FOREIGN KEY (project_id) REFERENCES projects(project_id) ON DELETE CASCADE)");
//
//        // Batteries table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS batteries (" +
//                "component_id INT PRIMARY KEY," +
//                "voltage FLOAT NOT NULL," +
//                "FOREIGN KEY (component_id) REFERENCES components(component_id) ON DELETE CASCADE)");
//
//        // Resistors table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS resistors (" +
//                "component_id INT PRIMARY KEY," +
//                "resistance FLOAT NOT NULL," +
//                "FOREIGN KEY (component_id) REFERENCES components(component_id) ON DELETE CASCADE)");
//
//        // Light bulbs table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS light_bulbs (" +
//                "component_id INT PRIMARY KEY," +
//                "resistance FLOAT NOT NULL," +
//                "FOREIGN KEY (component_id) REFERENCES components(component_id) ON DELETE CASCADE)");
//
//        // Switches table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS switches (" +
//                "component_id INT PRIMARY KEY," +
//                "is_active BOOLEAN NOT NULL," +
//                "FOREIGN KEY (component_id) REFERENCES components(component_id) ON DELETE CASCADE)");
//
//        // Wires table
//        statement.executeUpdate("CREATE TABLE IF NOT EXISTS wires (" +
//                "component_id INT PRIMARY KEY," +
//                "rx_cord INT NOT NULL," +
//                "ry_cord INT NOT NULL," +
//                "FOREIGN KEY (component_id) REFERENCES components(component_id) ON DELETE CASCADE)");
//    }


    public void queryUserByUsername(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listAllUsers() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users")) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Username: " + username + ", Email: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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
        String query = "SELECT id FROM users WHERE email = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");  // Login success: return user ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  //Login failed
    }

    public String getUsernameById(int userId) {
        String query = "SELECT username FROM users WHERE id = ?";
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
        String sql = "UPDATE users SET username = ? WHERE id = ?";
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
