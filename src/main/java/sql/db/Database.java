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
import java.util.Map;
import storables.Item;
import storables.User;

/**
 *
 * @author Janne
 */
public class Database {

    private String address;
    private String username;
    private String password;

    public Database(String driver, String address, String username, String password) throws ClassNotFoundException {
        Class.forName(driver);
        this.address = address;
        this.username = username;
        this.password = password;
    }
    
    public boolean canSelect(User user, Item item) {
        String sql = "SELECT * FROM Item as i, AccessControl as a, Users as u WHERE i.id = a.item_id AND u.id = a.users_id";
        return false;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address, username, password);
    }
    
    /**
     * 
     * @param sql
     * @param params
     * @return generated id
     * @throws SQLException 
     */
    public int update(String sql, Object... params) throws SQLException {
        int id = 0;
        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
            }
            ps.close();
        }
        
        return id;
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
