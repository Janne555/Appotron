/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import storables.Item;

/**
 *
 * @author Janne
 */
public class ItemDao {

    private Database db;

    public ItemDao(Database db) {
        this.db = db;
    }

    public Item findOne(String uuid) throws SQLException {
        try (Connection connection = db.getConnection()) {
            List<Item> queryAndCollect = db.queryAndCollect("SELECT "
                    + "Item.uuid, "
                    + "Item.name, "
                    + "Item.serial_number, "
                    + "Item.created_on, "
                    + "Item.location AS location_id, "
                    + "Location.name AS location_name "
                    + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                    + "WHERE Item.deleted = 'false' AND Item.uuid = ?", rs -> {
                        return new Item(rs.getString("uuid"),
                                rs.getString("name"),
                                rs.getString("serial_number"),
                                rs.getString("location_name"),
                                rs.getInt("location_id"),
                                new Timestamp(rs.getString("created_on")),
                                MetaDao.getDescriptions(rs.getString("serial_number"), connection),
                                MetaDao.getTags(rs.getString("serial_number"), rs.getString("uuid"), connection));
                    }, uuid);
            return queryAndCollect.get(0);
        }
    }
    
    public List<Item> findAll() throws SQLException {
        try (Connection connection = db.getConnection()) {
            List<Item> queryAndCollect = db.queryAndCollect("SELECT "
                    + "Item.uuid, "
                    + "Item.name, "
                    + "Item.serial_number, "
                    + "Item.created_on, "
                    + "Item.location AS location_id, "
                    + "Location.name AS location_name "
                    + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                    + "WHERE Item.deleted = 'false' AND Item.type = 'item'", rs -> {
                        return new Item(rs.getString("uuid"),
                                rs.getString("name"),
                                rs.getString("serial_number"),
                                rs.getString("location_name"),
                                rs.getInt("location_id"),
                                new Timestamp(rs.getString("created_on")),
                                MetaDao.getDescriptions(rs.getString("serial_number"), connection),
                                MetaDao.getTags(rs.getString("serial_number"), rs.getString("uuid"), connection));
                    });
            return queryAndCollect;
        }
    }

    public Item create(Item t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void update(String key, Item t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
