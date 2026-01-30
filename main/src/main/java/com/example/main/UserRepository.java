package com.example.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public boolean register(String login, String password) throws SQLException {
        String sql = "INSERT INTO users(login, password) VALUES(?, ?)";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, password); 
            ps.executeUpdate();
            return true;
        }
    }

    public boolean loginValid(String login, String password) throws SQLException {
        return loginGetUserId(login, password) != null;
    }

    public boolean userExists(String login) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE login = ?";
        try (Connection c = DB.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, login);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    
    public Integer loginGetUserId(String login, String password) throws SQLException {
    String sql = "SELECT id FROM users WHERE login=? AND password=?";
    try (Connection c = DB.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, login);
        ps.setString(2, password);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("id");
            return null;
        }
    }
}
    
    
    
    
    
    
    
    
    
    
}
