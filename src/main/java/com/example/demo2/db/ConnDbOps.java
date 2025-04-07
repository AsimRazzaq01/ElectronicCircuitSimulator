/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo2.db;

import java.sql.*;

/**
 *
 * @author MoaathAlrajab
 */
public class ConnDbOps {
    final String MYSQL_SERVER_URL = "jdbc:mysql://csc311asimserver.mysql.database.azure.com/";
    final String DB_URL = MYSQL_SERVER_URL + "DBname";
    final String USERNAME = "rasim";
    final String PASSWORD = "FARM123$";
    
    public  boolean connectToDatabase() {
        boolean hasRegistredUsers = false;


        //Class.forName("com.mysql.jdbc.Driver");
        try {
            //First, connect to MYSQL server and create the database if not created
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DBname");
            statement.close();
            conn.close();

            //Second, connect to the database and create the table "users" if cot created
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL UNIQUE,"
                    + "phone VARCHAR(200),"
                    + "address VARCHAR(200),"
                    + "password VARCHAR(200) NOT NULL"
                    + ")";
            statement.executeUpdate(sql);

            //check if we have users in the table users
            statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegistredUsers = true;
                }
            }

            statement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegistredUsers;
    }

    public  void queryUserByName(String name) {


        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users WHERE name = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Address: " + address);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void listAllUsers() {



        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM users ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String address = resultSet.getString("address");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phone + ", Address: " + address);
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  void insertUser(String name, String email, String phone, String address, String password) {


        try {
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO users (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, address);
            preparedStatement.setString(5, password);

            int row = preparedStatement.executeUpdate();

            if (row > 0) {
                System.out.println("A new user was inserted successfully.");
            }

            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
}
