/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Janne
 */
public class Database {
    private Connection connection;

    public Database(String driver, String address) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        this.connection = DriverManager.getConnection(address);
    }

    public Connection getConnection() {
        return connection;
    }

    public void update(String sql) throws SQLException {
        connection.setAutoCommit(false);
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
        connection.commit();
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        List<T> rows = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement("");
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
        
        ResultSet rs = stmt.executeQuery(query);
        
        while (rs.next()) {
            rows.add(col.collect(rs));
        }
        
        rs.close();
        stmt.close();
        return rows;
    }

    public void query(String query, Command com) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            com.act(rs);
        }
        rs.close();
        stmt.close();
    }

    public List<String> getDescriptions(String serial_or_isbn) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(serial_or_isbn);
        List<String> descriptions = new ArrayList<>();
        while (rs.next()) {
            descriptions.add(rs.getString("descriptor"));
        }
        rs.close();
        stmt.close();
        return descriptions;
    }

    public int getLocationKey(String value) throws SQLException {
        String sql = "SELECT * FROM Location WHERE name = \"" + value + "\"";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (!rs.next()) {
            return 0;
        }
        int result = rs.getInt("id");
        rs.close();
        stmt.close();
        return result;
    }

    public String getLocationValue(int key) throws SQLException {
        String sql = "SELECT * FROM Location WHERE id = " + key;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (!rs.next()) {
            return null;
        }
        String result = rs.getString("name");
        rs.close();
        stmt.close();
        return result;
    }
}
