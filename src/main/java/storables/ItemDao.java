/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sql.db.Database;
import sql.db.Search;
import sql.db.Timestamp;

/**
 *
 * @author Janne
 */
public class ItemDao {

    private Database db;

    public ItemDao(Database db) {
        this.db = db;
    }

    public List<Item> findAll() throws SQLException {
        List<Item> result;
        try (Connection connection = db.getConnection()) {
            ResultSet rs = connection.createStatement().executeQuery(
                    "SELECT "
                    + "Item.uuid, "
                    + "Item.name, "
                    + "Item.serial_number, "
                    + "Item.created_on, "
                    + "Item.location AS location_id, "
                    + "Location.name AS location_name "
                    + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                    + "WHERE Item.deleted = 'false' AND Item.type = 'item'");
            result = new ArrayList<>();
            while (rs.next()) {
                Map<String, String> tags = new HashMap<>();
                List<String> descriptions = new ArrayList<>();

                String uuid = rs.getString("uuid");
                String name = rs.getString("name");
                String serial_number = rs.getString("serial_number");
                Timestamp created_on = new Timestamp(rs.getString("created_on"));
                String location = rs.getString("location_name");
                int location_id = rs.getInt("location_id");
                
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM Tag WHERE serial_number = ?");
                statement.setString(1, serial_number);
                ResultSet rs1 = statement.executeQuery();
                while (rs1.next()) {
                    try {
                        tags.put(rs1.getString("key"), rs1.getString("value"));
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }
                
                statement = connection.prepareStatement(
                        "SELECT * FROM Description WHERE serial_number = ?");
                statement.setString(1, serial_number);
                rs1 = statement.executeQuery();
                while (rs1.next()) {
                    descriptions.add(rs1.getString("descriptor"));
                }
                
                result.add(new Item(uuid, name, serial_number, location, location_id, created_on, descriptions, tags));
            }
        }
        return result;
    }
}
