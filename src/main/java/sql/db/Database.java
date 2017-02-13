/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
    private DatabaseInfo databaseInfo;

    public Database(DatabaseInfo databaseInfo) throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + databaseInfo.getDatabaseName());
            System.out.println("Opened database '" + databaseInfo.getDatabaseName() + "' succesfully");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        this.databaseInfo = databaseInfo;
        init();
    }

    private void init() throws SQLException {
        File f = new File(databaseInfo.getDatabaseName());
        if (f.exists()) {
            return;
        }
        createDatabase();
    }

    public void createDatabase() throws SQLException {
        for (Table table : databaseInfo.getTables()) {
            createTable(table);
        }

    }

    public void createTable(Table table) throws SQLException {
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE " + table.getName() + " (";
        List<String> columnNames = table.getColumnNames();
        List<String> commands = table.getCommands();
        Iterator<String> iterator = commands.iterator();
        for (String columnName : columnNames) {
            String command = iterator.next();
            sql += columnName + " " + command;
            if (iterator.hasNext()) {
                sql += ", ";
            }
        }
        sql += ")";
        stmt.executeUpdate(sql);
        stmt.close();
        System.out.println("Table " + table.getName() + " created succesfully");
    }

    public void update(String sql) throws SQLException {
        connection.setAutoCommit(false);
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
        connection.commit();
    }

    public <T> List<T> queryAndCollect(String query, Collector col) throws SQLException {
        List<T> rows = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            rows.add((T) col.collect(rs));
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
