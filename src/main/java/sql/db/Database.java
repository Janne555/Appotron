/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public void update(String sql, Object... params) throws SQLException {
        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
//                System.out.println(params[i]);
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            ps.close();
        }
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        List<T> rows = new ArrayList<>();
        try (Connection connection = getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
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
}
