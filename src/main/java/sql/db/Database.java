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

    private String address;

    public Database(String driver, String address) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        this.address = address;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address);
    }

    public void update(String sql) throws SQLException {
        try (Connection connection = getConnection()) {
            connection.createStatement().executeUpdate(sql);
        }
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        List<T> rows = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                System.out.println(params[i]);
                stmt.setObject(i + 1, params[i]);
            }
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(col.collect(rs));
            }
            rs.close();
        }
        return rows;
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col) throws SQLException {
        List<T> rows = new ArrayList<>();
        try (Connection connection = getConnection()) {
            ResultSet rs = connection.createStatement().executeQuery(query);
            while (rs.next()) {
                rows.add((T) col.collect(rs));
            }
            rs.close();
        }
        return rows;
    }

    public void query(String query, Command com) throws SQLException {
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            com.act(rs);
        }
        rs.close();
        stmt.close();
    }

    public int getLocationKey(String value) throws SQLException {
        String sql = "SELECT * FROM Location WHERE name = \"" + value + "\"";
        Statement stmt = getConnection().createStatement();
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
        Statement stmt = getConnection().createStatement();
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
