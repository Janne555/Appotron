/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sql.db.Dao;
import sql.db.Database;
import sql.db.Search;
import sql.db.Timestamp;
import storables.Item;

/**
 *
 * @author Janne
 */
public class ItemDao implements Dao<Item, String> {

    private Database db;

    public ItemDao(Database db) {
        this.db = db;
    }

    @Override
    public Item findOne(String uuid) throws SQLException {
        try (Connection connection = db.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT "
                            + "Item.uuid, "
                            + "Item.name, "
                            + "Item.serial_number, "
                            + "Item.created_on, "
                            + "Item.location AS location_id, "
                            + "Location.name AS location_name "
                            + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                            + "WHERE Item.deleted = 'false' AND Item.uuid = ?");
            ps.setObject(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String serial_number = rs.getString("serial_number");
                Timestamp created_on = new Timestamp(rs.getString("created_on"));
                String location = rs.getString("location_name");
                int location_id = rs.getInt("location_id");

                Map<String, String> tags = MetaDao.getTags(serial_number, connection);
                List<String> descriptions = MetaDao.getDescriptions(serial_number, connection);
                
                ps.close();
                rs.close();
                return new Item(uuid, name, serial_number, location, location_id, created_on, descriptions, tags);
            }
        }
        return null;
    }

    @Override
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

                String uuid = rs.getString("uuid");
                String name = rs.getString("name");
                String serial_number = rs.getString("serial_number");
                Timestamp created_on = new Timestamp(rs.getString("created_on"));
                String location = rs.getString("location_name");
                int location_id = rs.getInt("location_id");

                Map<String, String> tags = MetaDao.getTags(serial_number, connection);
                List<String> descriptions = MetaDao.getDescriptions(serial_number, connection);

                result.add(new Item(uuid, name, serial_number, location, location_id, created_on, descriptions, tags));
            }
            rs.close();
        }
        return result;
    }

    @Override
    public Item create(Item t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(String key, Item t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
