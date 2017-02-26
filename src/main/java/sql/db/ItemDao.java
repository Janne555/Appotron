/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        this.select = "SELECT "
                + "Item.uuid, "
                + "Item.name, "
                + "Item.serial_number, "
                + "Item.created_on, "
                + "Item.location AS location_id, "
                + "Location.name AS location_name, "
                + "Item.type "
                + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                + "WHERE Item.deleted = 'false'";
    }

    public Item findOne(String uuid) throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.uuid = ?", rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location_name"),
                    rs.getInt("location_id"),
                    new Timestamp(rs.getString("created_on")),
                    tagDao.findAllByIdentifier(rs.getString("uuid")),
                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
                    Type.parseType(rs.getString("type")));
        }, uuid);
        return queryAndCollect.get(0);
    }

    public List<Item> findAll() throws SQLException {
        List<Item> queryAndCollect = db.queryAndCollect(select, rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location_name"),
                    rs.getInt("location_id"),
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
                    rs.getString("location_name"),
                    rs.getInt("location_id"),
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

    public void create(Item t) throws SQLException {
        db.update("INSERT INTO Item(uuid, name, serial_number, location, created_on, type, deleted VALUES(?,?,?,?,?,?,?)", t.getObjs());
        tagDao.create(t.getTags());
    }

    public void update(String key, Item t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
