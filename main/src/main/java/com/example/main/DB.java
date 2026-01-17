
package com.example.main;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    
    
    //jak dodamy użytkownika, to aby był w bazie danych widoczny to trzeba odświeżyć dane
    //komenda:
    //SELECT * FROM users;

    private static final String URL = "jdbc:mysql://localhost:3306/glovo_app?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
