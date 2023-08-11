package com.example.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    static Connection conn = null;

    public static Properties lProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static Connection gConnection() {
        try {
            if (conn == null) {

                Properties props = lProperties();
                String url = props.getProperty("url");
                String username = props.getProperty("username");
                String password = props.getProperty("password");

                conn = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return conn;
    }
}
