/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sql.db.Database;
import util.Param;
import util.Type;
import storables.Item;

/**
 *
 * @author Janne
 */
public class ItemDao {

    private String select;
    private Database db;
    private TagDao tagDao;

    public ItemDao(Database db) {
        this.db = db;
        this.tagDao = new TagDao(db);
        this.select = "SELECT * FROM Item WHERE Item.deleted = false";
    }

    public Item findOne(String uuid) throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.uuid = ?", rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("expiration"),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.getType(rs.getString("type")));
        }, uuid);
        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public Item findOneBySerial(String serial) throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.serial_number = ?", rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("expiration"),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.getType(rs.getString("type")));
        }, serial);
        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public List<Item> findAll() throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select, rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("expiration"),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.getType(rs.getString("type")));
        });
        return queryAndCollect;
    }

    public List<String> getLocations() throws SQLException {
        List<String> queryAndCollect = db.queryAndCollect("SELECT DISTINCT location FROM Item ORDER BY location ASC", rs -> {
            return rs.getString("location");
        });
        return queryAndCollect;
    }

    public void create(Item t) throws SQLException {
        db.update("INSERT INTO Item(uuid, name, serial_number, location, created_on, expiration, type, deleted) VALUES(?,?,?,?,?,?,?,?)", t.getObjs());
        tagDao.create(t.getTags());
    }

    public void update(Item t) throws SQLException {
        db.update("UPDATE Item SET name = ?, serial_number = ?, location = ?, created_on = ?, expiration = ?, type = ?, deleted = ? WHERE uuid = ?", t.getObjs());
    }

    public void delete(String key) throws SQLException {
        db.update("UPDATE Item SET deleted = true WHERE uuid = ?", key);
    }

    public List<Item> getExpiring(int number) throws SQLException {
        List<Item> items = db.queryAndCollect(select + " AND expiration IS NOT NULL ORDER BY expiration ASC LIMIT ?", rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    rs.getTimestamp("created_on"),
                    rs.getTimestamp("expiration"),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.getType(rs.getString("type")));
        }, number);

        return items;
    }
    
/**
    public List<Item> searchIngredients(String param) throws SQLException {
        param = "%" + param + "%";
        Object[] objs = new Object[5];
        for (int i = 0; i < objs.length; i++) {
            objs[i] = param;
        }
        List<Item> items = db.queryAndCollect("SELECT DISTINCT ON (serial_number) * FROM Item WHERE deleted = 'false'"
                + " AND type = 'foodstuff'"
                + " AND uuid LIKE ? "
                + "OR serial_number LIKE ? "
                + "OR name LIKE ? "
                + "OR type LIKE ? "
                + "OR location LIKE ? LIMIT 10", rs -> {
                    return new Item(rs.getString("uuid"),
                            rs.getString("name"),
                            rs.getString("serial_number"),
                            rs.getString("location"),
                            rs.getTimestamp("created_on"),
                            rs.getTimestamp("expiration"),
                            tagDao.findAllByIdentifier(rs.getString("uuid")),
                            tagDao.findAllByIdentifier(rs.getString("serial_number")),
                            Type.parseType(rs.getString("type")));
                }, objs);

        return items;
    }
**/
    
    public List<Item> search(String... searchWords) throws SQLException {
        if (searchWords.length == 0) {
            return null;
        }
        
        String sql = "SELECT * FROM "
                + "(SELECT item.*, "
                + "to_tsvector(item.uuid) || "
                + "to_tsvector(item.name) || "
                + "to_tsvector(item.serial_number) || "
                + "to_tsvector(item.location) || "
                + "to_tsvector(item.type) || "
                + "to_tsvector(coalesce((string_agg(tag.value, '')), '')) "
                + "AS document "
                + "FROM Item LEFT JOIN Tag ON tag.identifier = item.serial_number "
                + "WHERE item.deleted = 'false' GROUP BY item.uuid) i_search "
                + "WHERE i_search.document @@ to_tsquery(?)";
        
        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND i_search.document @@ to_tsquery(?)";
        }
        
        List<Item> items = db.queryAndCollect(sql, rs -> {
                    return new Item(rs.getString("uuid"),
                            rs.getString("name"),
                            rs.getString("serial_number"),
                            rs.getString("location"),
                            rs.getTimestamp("created_on"),
                            rs.getTimestamp("expiration"),
                            tagDao.findAllByIdentifier(rs.getString("uuid")),
                            tagDao.findAllByIdentifier(rs.getString("serial_number")),
                            Type.getType(rs.getString("type")));
                }, searchWords);

        return items;
    }
}
