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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(address, username, password);
    }

    public int update(String sql, boolean returnId, Object... params) throws SQLException {
        int id = 0;
        try (Connection connection = getConnection()) {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next() && returnId) {
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

    public boolean canEdit(String personIdentifier, int itemId) throws SQLException {
        return !queryAndCollect("SELECT * FROM item, permission WHERE item.id = permission.item_id AND permission.canedit = 'true' AND permission.person_identifier = ? AND item.id = ?", rs -> {
            return rs.getString(1);
        }, personIdentifier, itemId).isEmpty();
    }

    public boolean canDelete(String personIdentifier, int itemId) throws SQLException {
        return !queryAndCollect("SELECT * FROM item, permission WHERE item.id = permission.item_id AND permission.candelete = 'true' AND permission.person_identifier = ? AND item.id = ?", rs -> {
            return rs.getString(1);
        }, personIdentifier, itemId).isEmpty();
    }
}
