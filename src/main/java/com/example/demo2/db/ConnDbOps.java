package com.example.demo2.db;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;


public class ConnDbOps {
    private static final String MYSQL_SERVER_URL = "jdbc:mysql://csc311mojica04.mysql.database.azure.com/";
    private static final String DB_URL = MYSQL_SERVER_URL + "DBname";
    private static final String USERNAME = "mojin";
    private static final String PASSWORD = "FARM123$";

    public boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        try {
            // Step 1: Create database if it doesn't exist
            Connection conn = getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            statement.close();
            conn.close();

            // Step 2: Connect to DB and create tables
            conn = getConnection(DB_URL, USERNAME, PASSWORD);
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

    public void queryUserByUsername(String username) {
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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

        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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

        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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

        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
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

    public int getProjectIdByName(int userId, String projectName) {
        String sql = "SELECT project_id FROM projects WHERE user_id = ? AND project_name = ?";
        try (Connection conn = getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, projectName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("project_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // or throw if preferred
    }


    public static void saveComponent(Project project, Component component) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (!component.hasValidID()) {
                String insertSql = "INSERT INTO components (project_id, component_type, x_cord, y_cord) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

                insertStmt.setInt(1, project.getProjectID());
                insertStmt.setString(2, component.getComponentType());
                insertStmt.setInt(3, (int) component.getComponentX());
                insertStmt.setInt(4, (int) component.getComponentY());

                insertStmt.executeUpdate();

                ResultSet rs = insertStmt.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    component.setComponentID(generatedId);

                    insertIntoSubtypeTable(conn, component); // Insert into batteries, wires, etc.
                }

            } else {
                // UPDATE position of existing component
                String updateSql = "UPDATE components SET x_cord = ?, y_cord = ? WHERE component_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, (int) component.getComponentX());
                    updateStmt.setInt(2, (int) component.getComponentY());
                    updateStmt.setInt(3, component.getComponentID());
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void insertIntoSubtypeTable(Connection conn, Component component) throws SQLException {
        switch (component.getComponentType()) {
            case "Battery" -> {
                BatteryModel battery = (BatteryModel) component;
                String sql = "INSERT INTO batteries (component_id, voltage) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, battery.getComponentID());
                    stmt.setDouble(2, battery.getVoltage());
                    stmt.executeUpdate();
                }
            }
            case "Switch" -> {
                CircuitSwitchModel sw = (CircuitSwitchModel) component;
                String sql = "INSERT INTO switches (component_id, is_active) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, sw.getComponentID());
                    stmt.setBoolean(2, sw.isActive());
                    stmt.executeUpdate();
                }
            }
            case "Wire" -> {
                WireModel wire = (WireModel) component;
                String sql = "INSERT INTO wires (component_id, rx_cord, ry_cord) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, wire.getComponentID());
                    stmt.setInt(2, (int) wire.getRightSideX());
                    stmt.setInt(3, (int) wire.getRightSideY());
                    stmt.executeUpdate();
                }
            }
            case "Resistor" -> {
                ResistorModel resistor = (ResistorModel) component;
                String sql = "INSERT INTO resistors (component_id, resistance) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, resistor.getComponentID());
                    stmt.setDouble(2, resistor.getResistance());
                    stmt.executeUpdate();
                }
            }
            case "Lightbulb" -> {
                LightbulbModel bulb = (LightbulbModel) component;
                String sql = "INSERT INTO light_bulbs (component_id, resistance) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, bulb.getComponentID());
                    stmt.setDouble(2, bulb.getResistance());
                    stmt.executeUpdate();
                }
            }
            default -> {
                System.out.println(" Unrecognized component type: " + component.getComponentType());
            }
        }
    }

    public static List<Component> loadComponentsForProject(int projectId) {
        List<Component> components = new ArrayList<>();

        String baseQuery = "SELECT * FROM components WHERE project_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(baseQuery)) {

            stmt.setInt(1, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int componentId = rs.getInt("component_id");
                String type = rs.getString("component_type");
                int x = rs.getInt("x_cord");
                int y = rs.getInt("y_cord");

                Component component = switch (type) {
                    case "Battery" -> {
                        PreparedStatement batteryStmt = conn.prepareStatement("SELECT voltage FROM batteries WHERE component_id = ?");
                        batteryStmt.setInt(1, componentId);
                        ResultSet brs = batteryStmt.executeQuery();
                        double voltage = brs.next() ? brs.getDouble("voltage") : 9.0;
                        BatteryModel b = new BatteryModel(x, y, voltage);
                        b.setComponentID(componentId);
                        yield b;
                    }
                    case "Resistor" -> {
                        PreparedStatement rStmt = conn.prepareStatement("SELECT resistance FROM resistors WHERE component_id = ?");
                        rStmt.setInt(1, componentId);
                        ResultSet rrs = rStmt.executeQuery();
                        double resistance = rrs.next() ? rrs.getDouble("resistance") : 10.0;
                        ResistorModel r = new ResistorModel(x, y, resistance);
                        r.setComponentID(componentId);
                        yield r;
                    }
                    case "Lightbulb" -> {
                        PreparedStatement lbStmt = conn.prepareStatement("SELECT resistance FROM light_bulbs WHERE component_id = ?");
                        lbStmt.setInt(1, componentId);
                        ResultSet lrs = lbStmt.executeQuery();
                        double resistance = lrs.next() ? lrs.getDouble("resistance") : 10.0;
                        LightbulbModel l = new LightbulbModel(x, y, resistance);
                        l.setComponentID(componentId);
                        yield l;
                    }
                    case "Switch" -> {
                        PreparedStatement sStmt = conn.prepareStatement("SELECT is_active FROM switches WHERE component_id = ?");
                        sStmt.setInt(1, componentId);
                        ResultSet srs = sStmt.executeQuery();
                        boolean active = srs.next() && srs.getBoolean("is_active");
                        CircuitSwitchModel sw = new CircuitSwitchModel(x, y, active);
                        sw.setComponentID(componentId);
                        yield sw;
                    }
                    case "Wire" -> {
                        PreparedStatement wStmt = conn.prepareStatement("SELECT rx_cord, ry_cord FROM wires WHERE component_id = ?");
                        wStmt.setInt(1, componentId);
                        ResultSet wrs = wStmt.executeQuery();
                        double rx = 0, ry = 0;
                        if (wrs.next()) {
                            rx = wrs.getInt("rx_cord");
                            ry = wrs.getInt("ry_cord");
                        }
                        WireModel wire = new WireModel(x, y, rx, ry);
                        wire.setComponentID(componentId);
                        yield wire;
                    }
                    default -> null;
                };

                if (component != null) {
                    components.add(component);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return components;
    }




}
