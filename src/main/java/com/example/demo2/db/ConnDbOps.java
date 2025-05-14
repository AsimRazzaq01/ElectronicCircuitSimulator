package com.example.demo2.db;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.*;
import javafx.scene.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ConnDbOps -> DB class -> class to allow us to access our Azure SQL database
 */
public class ConnDbOps {
    // Connection to azure db account
    private static final String MYSQL_SERVER_URL = "jdbc:mysql://csc311mojica04.mysql.database.azure.com/";
    private static final String DB_URL = MYSQL_SERVER_URL + "DBname";
    private static final String USERNAME = "mojin";
    private static final String PASSWORD = "FARM123$";

    /**
     * method to query by usernames in db
     * @param username
     */
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
    } // End queryUserByUsername method

    /**
     * method to listAllUsers
     */
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
    } // End listAllUsers method


    /**
     * Method to insert users into db
     * @param username
     * @param email
     * @param password
     * @return -> 0 if successful else -1
     */
    public int insertUser(String username, String email, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = conn.prepareStatement(
                     "INSERT INTO users (username, email, password) VALUES (?, ?, ?)")) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return -1;
        }

        return 0;
    } // End insertUser method

    /**
     * Method to confirm users are in the db / get there id
     * @param email
     * @param password
     * @return -> id or -1 if Login failed, -2 if error
     */
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
        } catch (SQLException _) {
            return -2;
        }
        return -1;  //Login failed
    } // End validateLoginAndGetUserId method

    /**
     * method to get Username By Id
     * @param userId
     * @return -> Username
     */
    public String getUsernameById(int userId) {
        String query = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException _) {
        }
        return null;
    } // End getUsernameById method

    /**
     * method -> to update Username By Id
     * @param userId
     * @param newUsername
     */
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
    } // End updateUsernameById method

    /**
     * method -> to insert Project into db of user
     * @param userId
     * @param projectName
     * @return -> 0 if successful, -1 if exception occurred.
     */
    public int insertProject(int userId, String projectName) {
        String sql = "INSERT INTO projects (user_id, project_name) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, projectName);
            pstmt.executeUpdate();

            System.out.println("Project inserted successfully.");

        } catch (SQLException e) {
            return -1;
        }
        return 0;
    } // End insertProject method

    /**
     * Deletes a project from the database based on the user ID and project name.
     * @param userId
     * @param projectName
     */
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
    } // End deleteProject method

    /**
     * method -> to delete Component
     * @param component
     */
    public static void deleteComponent(Component component) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            switch (component.getComponentType()) {
                case "Wire" -> {
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM wires WHERE component_id = ?")) {
                        stmt.setInt(1, component.getComponentID());
                        stmt.executeUpdate();
                    }
                }
                case "Battery" -> {
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM batteries WHERE component_id = ?")) {
                        stmt.setInt(1, component.getComponentID());
                        stmt.executeUpdate();
                    }
                }
                case "Resistor" -> {
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM resistors WHERE component_id = ?")) {
                        stmt.setInt(1, component.getComponentID());
                        stmt.executeUpdate();
                    }
                }
                case "Lightbulb" -> {
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM lightbulbs WHERE component_id = ?")) {
                        stmt.setInt(1, component.getComponentID());
                        stmt.executeUpdate();
                    }
                }
                case "Switch" -> {
                    try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM switches WHERE component_id = ?")) {
                        stmt.setInt(1, component.getComponentID());
                        stmt.executeUpdate();
                    }
                }
            }
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM components WHERE component_id = ?")) {
                stmt.setInt(1, component.getComponentID());
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    } // End deleteComponent method

    /**
     * method -> to get as saved Projects For User in db
     * @param userId
     * @return -> project
     */
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
    } // End getProjectsForUser method

    /**
     * Retrieves the project ID from the database based on the user ID and project name.
     * @param userId
     * @param projectName
     * @return -> The project ID if found, -1 if no matching project exists,
     *          -2 if a SQL exception occurs.
     */
    public int getProjectIdByName(int userId, String projectName) {
        String sql = "SELECT project_id FROM projects WHERE user_id = ? AND project_name = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, projectName);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("project_id");
            }

        } catch (SQLException e) {
            return -2;
        }
        return -1;
    } // End getProjectIdByName method

    //Modified loadComponents
    public static HashMap<Component, Node> loadComponentsForProject(int projectId) {
        HashMap<Component, Node> components = new HashMap<>();

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

                switch (type) {
                    case "Battery" -> {
                        PreparedStatement batteryStmt = conn.prepareStatement("SELECT voltage FROM batteries WHERE component_id = ?");
                        batteryStmt.setInt(1, componentId);
                        ResultSet brs = batteryStmt.executeQuery();
                        double voltage = brs.next() ? brs.getDouble("voltage") : 9.0;
                        BatteryNode batteryNode = new BatteryNode(x, y, voltage);
                        BatteryModel b = batteryNode.getBatteryModel();
                        b.setComponentID(componentId);
                        components.put(b, batteryNode);
                    }
                    case "Resistor" -> {
                        PreparedStatement rStmt = conn.prepareStatement("SELECT resistance FROM resistors WHERE component_id = ?");
                        rStmt.setInt(1, componentId);
                        ResultSet rrs = rStmt.executeQuery();
                        double resistance = rrs.next() ? rrs.getDouble("resistance") : 10.0;
                        ResistorNode resistorNode = new ResistorNode(x, y, resistance);
                        ResistorModel r = resistorNode.getResistorModel();
                        r.setComponentID(componentId);
                        components.put(r, resistorNode);
                    }
                    case "Light bulb" -> {
                        PreparedStatement lbStmt = conn.prepareStatement("SELECT resistance FROM light_bulbs WHERE component_id = ?");
                        lbStmt.setInt(1, componentId);
                        ResultSet lrs = lbStmt.executeQuery();
                        double resistance = lrs.next() ? lrs.getDouble("resistance") : 10.0;
                        LightbulbNode lightbulbNode = new LightbulbNode(x, y, resistance);
                        LightbulbModel l = lightbulbNode.getLightbulbModel();
                        l.setComponentID(componentId);
                        components.put(l, lightbulbNode);
                    }
                    case "Switch" -> {
                        PreparedStatement sStmt = conn.prepareStatement("SELECT is_active FROM switches WHERE component_id = ?");
                        sStmt.setInt(1, componentId);
                        ResultSet srs = sStmt.executeQuery();
                        boolean active = srs.next() && srs.getBoolean("is_active");
                        CircuitSwitchNode switchNode = new CircuitSwitchNode(x, y, active);
                        CircuitSwitchModel sw = switchNode.getSwitchModel();
                        sw.setComponentID(componentId);
                        components.put(sw, switchNode);
                    }
                    case "Wire" -> {
                        PreparedStatement wStmt = conn.prepareStatement("SELECT rx_cord, ry_cord FROM wires WHERE component_id = ?");
                        wStmt.setInt(1, componentId);
                        ResultSet wrs = wStmt.executeQuery();
                        double rx = 0, ry = 0;
                        if (wrs.next()) {
                            rx = wrs.getDouble("rx_cord");
                            ry = wrs.getDouble("ry_cord");
                        }
                        WireNode wireNode = new WireNode(x, y, rx, ry);
                        WireModel w = wireNode.getWireModel();
                        w.setComponentID(componentId);
                        components.put(w, wireNode);
                    }
                };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return components;
    } // End loadComponentsForProject method

    /**
     * Inserts a component's specific data into its corresponding subtype table in the database.
     * @param conn
     * @param component
     * @throws SQLException
     */
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
            case "Light bulb" -> {
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
    } // End insertIntoSubtypeTable method

    /**
     * Updates the specified value of a component in its corresponding subtype table.
     * @param component
     * @param value
     * @return -> 0 if the update was successful, -1 if a SQL exception
     */
    public static int updateComponents(Component component, Double value) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            switch (component.getComponentType()) {
                case "Battery" -> {
                    String updateSQL = "UPDATE batteries SET voltage = ? WHERE component_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(updateSQL);
                    stmt.setDouble(1, value);
                    stmt.setInt(2, component.getComponentID());

                    stmt.executeUpdate();
                }
                case "Resistor" -> {
                    String updateSQL = "UPDATE resistors SET resistance = ? WHERE component_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(updateSQL);

                    stmt.setDouble(1, value);
                    stmt.setInt(2, component.getComponentID());

                    stmt.executeUpdate();
                }
                case "Light bulb" -> {
                    String updateSql = "UPDATE light_bulbs SET resistance = ? WHERE component_id = ?";
                    PreparedStatement stmt = conn.prepareStatement(updateSql);

                    stmt.setDouble(1, value);
                    stmt.setInt(2, component.getComponentID());

                    stmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            return -1;
        }
        return 0;
    } // End updateComponents method

    /**
     * updateSwitches -> Updates the active state of a switch component in the database.
     * @param component
     * @param value
     * @return -> 0 if the update was successful, -1 if a SQL exception
     */
    public static int updateSwitches(Component component, Boolean value) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String updateSQL = "UPDATE switches SET is_active = ? WHERE component_id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSQL);

            stmt.setBoolean(1, value);
            stmt.setInt(2, component.getComponentID());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            return -1;
        }
        return 0;
    } // End updateSwitches method

    /**
     * Saves a component to the database by either inserting a new record or updating an existing one, depending on whether the component has a valid ID.
     * @param project
     * @param component
     */
    public static void saveComponent(Project project, Component component) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            if (!component.hasValidID()) {
                // INSERT into components table
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

                    // INSERT into subtype table (e.g., wires, batteries)
                    insertIntoSubtypeTable(conn, component);
                }
            } else {
                // UPDATE existing component position in components table
                String updateSql = "UPDATE components SET x_cord = ?, y_cord = ? WHERE component_id = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setInt(1, (int) component.getComponentX());
                    updateStmt.setInt(2, (int) component.getComponentY());
                    updateStmt.setInt(3, component.getComponentID());
                    updateStmt.executeUpdate();
                }

                // If it's a wire, also update the wires table
                if (component instanceof WireModel wire) {
                    String updateWireSql = "UPDATE wires SET rx_cord = ?, ry_cord = ? WHERE component_id = ?";
                    try (PreparedStatement wireUpdateStmt = conn.prepareStatement(updateWireSql)) {
                        wireUpdateStmt.setDouble(1, wire.getRightSideX());
                        wireUpdateStmt.setDouble(2, wire.getRightSideY());
                        wireUpdateStmt.setInt(3, wire.getComponentID());
                        wireUpdateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException _) {
        }
    } // End saveComponent method


} // End ConnDbOps class
