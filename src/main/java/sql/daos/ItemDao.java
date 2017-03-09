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
import util.Timestamp;
import util.Type;
import storables.Item;
import storables.Tag;

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
        this.select = "SELECT * FROM Item WHERE Item.deleted = 'false'";
    }

    public Item findOne(String uuid) throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.uuid = ?", rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    new Timestamp(rs.getString("created_on")),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.parseType(rs.getString("type")));
        }, uuid);
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
                    new Timestamp(rs.getString("created_on")),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.parseType(rs.getString("type")));
        });
        return queryAndCollect;
    }

    public List<Item> findBy(Map<Param, String> terms) throws SQLException {
        String query = select;
        if (!terms.isEmpty()) {
            query += " AND ";
        }

        List<String> values = new ArrayList<>();
        for (Param search : terms.keySet()) {
            query += search.getParam() + " AND ";
            values.add(terms.get(search));
        }
        if (!terms.isEmpty()) {
            query = query.substring(0, query.length() - 5);
        }

        System.out.println(query);

        List<Item> queryAndCollect = db.queryAndCollect(query, rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    new Timestamp(rs.getString("created_on")),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.parseType(rs.getString("type")));
        }, values.toArray());
        return queryAndCollect;
    }

    public List<Item> tagSearch(String key, String value) throws SQLException {
        List<String> queryAndCollect = db.queryAndCollect("SELECT serial_number FROM Tag WHERE key = ? AND value = ?", rs -> {
            return rs.getString("serial_number");
        }, key, value);
        HashMap<Param, String> terms = new HashMap<>();
        for (String s : queryAndCollect) {
            System.out.println(s);
            terms.put(Param.SERIAL, s);
        }
        return findBy(terms);
    }
    
    public List<String> getLocations() throws SQLException {
        List<String> queryAndCollect = db.queryAndCollect("SELECT DISTINCT location FROM Item ORDER BY location ASC", rs -> {
            return rs.getString("location");
        });
        return queryAndCollect;
    }

    public void create(Item t) throws SQLException {
        db.update("INSERT INTO Item(uuid, name, serial_number, location, created_on, type, deleted) VALUES(?,?,?,?,?,?,?)", t.getObjs());
        tagDao.create(t.getTags());
    }

    public void update(Item t) throws SQLException {
        db.update("UPDATE Item SET name = ?, serial_number = ?, location = ?, created_on = ?, type = ?, deleted = ? WHERE uuid = ?", t.getObjs());
    }

    public void delete(String key) throws SQLException {
        db.update("UPDATE Item SET deleted = 'true' WHERE uuid = ?", key);
    }
    
    public List<Item> getExpiring(int number) throws SQLException {
        List<Tag> tags = db.queryAndCollect("SELECT * FROM Tag WHERE key = 'expiration' ORDER BY value ASC LIMIT ?", rs -> {
            return new Tag(rs.getInt("id"), rs.getString("identifier"), rs.getString("key"), rs.getString("value"), rs.getString("type"));
        }, number);
        
        List<Item> items = new ArrayList<>();
        for (Tag tag : tags) {
            items.add(findOne(tag.getIdentifier()));
        }
        
        return items;
    }
}
