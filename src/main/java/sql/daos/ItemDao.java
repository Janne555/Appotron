/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Item;
import storables.ItemInfo;
import storables.User;
import util.Type;

/**
 *
 * @author Janne
 */
public class ItemDao {
    private Database db;
    private ItemInfoDao infoDao;
    private ItemSpecificTagDao tagDao;

    public ItemDao(Database db) {
        this.db = db;
        this.infoDao = new ItemInfoDao(db);
        this.tagDao = new ItemSpecificTagDao(db);
    }
    
    public Item store(Item t, User user) throws Exception {
        if (t.getItemInfo().getId() == 0) {
            //if true the iteminfo could not have been retrieved from the database so it propably doesn't exist there
            ItemInfo store = infoDao.store(t.getItemInfo());
            //returned iteminfo object should know its id
            t.setItemInfo(store);
        }
        //now we can be sure that the iteminfo exist in the database
        
        //we store the item in the database
        int update = db.update("INSERT INTO Item(iteminfo_id, location, date, expiration, deleted) VALUES(?, ?, ?, ?, ?)",
                t.getItemInfo().getId(), t.getLocation(), t.getDate(), t.getExpiration(), "false");
        
        t.setId(update);
        
        //next we create an accescontrol entry so we can search and edit this item later
        db.update("INSERT INTO AccessControl(item_id, users_id, level) VALUES(?,?,?)", t.getId(), user.getId(), 3);
        
        //we try to store the item's tags
        tagDao.store(t.getSpecificTags());
        
        return t;
    }

    public List<Item> search(User user, Object... searchWords) throws Exception {
        Object[] terms = new Object[searchWords.length + 1];
        terms[0] = user.getId();
        for (int i = 1; i < terms.length; i++) {
            terms[i] = searchWords[i - 1];
        }
        
        String sql = "SELECT DISTINCT ON (id) * FROM "
                + "(SELECT query.*, "
                + "to_tsvector(location) || "
                + "to_tsvector(type) || "
                + "to_tsvector(name) || "
                + "to_tsvector(identifier) || "
                + "to_tsvector(coalesce((string_agg(infotag, '')), '')) || "
                + "to_tsvector(coalesce((string_agg(specifictag, '')), '')) as document "
                + "FROM (SELECT iteminfo.id as info_id, date, expiration, item.id as id, location, type, name, identifier, iteminfotag.value as infotag, itemspecifictag.value as specifictag "
                + "FROM (SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE i.id = a.item_id AND u.id = a.users_id AND u.id = ?) AS item "
                + "LEFT JOIN iteminfo ON item.iteminfo_id = iteminfo.id "
                + "LEFT JOIN iteminfotag ON item.iteminfo_id = iteminfotag.iteminfo_id "
                + "LEFT JOIN itemspecifictag ON item.id = itemspecifictag.item_id "
                + "WHERE deleted = false) AS query GROUP BY query.date, query.expiration, query.info_id, query.location, query.type, query.name, query.identifier, query.id, query.infotag, query.specifictag) AS mainquery "
                + "WHERE mainquery.document @@ to_tsquery(?)";

        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND mainquery.document @@ to_tsquery(?)";
        }

        List<Item> items = db.queryAndCollect(sql, rs -> {
            return new Item(rs.getInt("id"),
                    rs.getString("location"),
                    rs.getTimestamp("date"),
                    rs.getTimestamp("expiration"),
                    tagDao.findAll(rs.getInt("id")),
                    infoDao.findOne(rs.getInt("info_id")));
        }, terms);
        

        return items;
    }

    public Item findOne(Integer key, User user) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public List<Item> findAll(User user) throws SQLException {
        String sql = "SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE i.id = a.item_id AND u.id = a.users_id AND u.id = ?";
        return db.queryAndCollect(sql, rs -> {
            return new Item(rs.getInt("i.id"),
                    rs.getString("i.location"),
                    rs.getTimestamp("i.date"),
                    rs.getTimestamp("i.expiration"), 
                    tagDao.findAll(rs.getInt("i.id")),
                    infoDao.findOne(rs.getInt("i.iteminfo_id")));
        }, user.getId());
    }

    public void update(Item t, User user) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(Integer key, User user) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//
//    @Override
//    public Item findOne(Integer key, Database db) throws SQLException {
//
//        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.uuid = ?", rs -> {
//            return new Item(rs.getString("uuid"),
//                    rs.getString("name"),
//                    rs.getString("serial_number"),
//                    rs.getString("location"),
//                    rs.getTimestamp("created_on"),
//                    rs.getTimestamp("expiration"),
//                    tagDao.findAllByIdentifier(rs.getString("uuid")),
//                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
//                    Type.getType(rs.getString("type")));
//        }, uuid);
//        if (queryAndCollect.isEmpty()) {
//            return null;
//        }
//
//        return queryAndCollect.get(0);
//        return null;
//    }
//
//    public Item findOneBySerial(String serial) throws SQLException {
//        List<Item> queryAndCollect = db.queryAndCollect(select + " AND Item.serial_number = ?", rs -> {
//            return new Item(rs.getString("uuid"),
//                    rs.getString("name"),
//                    rs.getString("serial_number"),
//                    rs.getString("location"),
//                    rs.getTimestamp("created_on"),
//                    rs.getTimestamp("expiration"),
//                    tagDao.findAllByIdentifier(rs.getString("uuid")),
//                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
//                    Type.getType(rs.getString("type")));
//        }, serial);
//        if (queryAndCollect.isEmpty()) {
//            return null;
//        }
//
//        return queryAndCollect.get(0);
//    }
//
//    public List<Item> findAll() throws SQLException {
//        List<Item> queryAndCollect = db.queryAndCollect(select, rs -> {
//            return new Item(rs.getString("uuid"),
//                    rs.getString("name"),
//                    rs.getString("serial_number"),
//                    rs.getString("location"),
//                    rs.getTimestamp("created_on"),
//                    rs.getTimestamp("expiration"),
//                    tagDao.findAllByIdentifier(rs.getString("uuid")),
//                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
//                    Type.getType(rs.getString("type")));
//        });
//        return queryAndCollect;
//    }
//
//    public List<String> getLocations() throws SQLException {
//        List<String> queryAndCollect = db.queryAndCollect("SELECT DISTINCT location FROM Item ORDER BY location ASC", rs -> {
//            return rs.getString("location");
//        });
//        return queryAndCollect;
//    }
//
//    public void create(Item t) throws SQLException {
//        db.update("INSERT INTO Item(uuid, name, serial_number, location, created_on, expiration, type, deleted) VALUES(?,?,?,?,?,?,?,?)", t.getObjs());
//        tagDao.create(t.getTags());
//    }
//
//    public void update(Item t) throws SQLException {
//        db.update("UPDATE Item SET name = ?, serial_number = ?, location = ?, created_on = ?, expiration = ?, type = ?, deleted = ? WHERE uuid = ?", t.getObjs());
//    }
//
//    public void delete(String key) throws SQLException {
//        db.update("UPDATE Item SET deleted = true WHERE uuid = ?", key);
//    }
//
//    public List<Item> getExpiring(int number) throws SQLException {
//        List<Item> items = db.queryAndCollect(select + " AND expiration IS NOT NULL ORDER BY expiration ASC LIMIT ?", rs -> {
//            return new Item(rs.getString("uuid"),
//                    rs.getString("name"),
//                    rs.getString("serial_number"),
//                    rs.getString("location"),
//                    rs.getTimestamp("created_on"),
//                    rs.getTimestamp("expiration"),
//                    tagDao.findAllByIdentifier(rs.getString("uuid")),
//                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
//                    Type.getType(rs.getString("type")));
//        }, number);
//
//        return items;
//    }
    /**
     * public List<Item> searchIngredients(String param) throws SQLException {
     * param = "%" + param + "%"; Object[] objs = new Object[5]; for (int i = 0;
     * i < objs.length; i++) { objs[i] = param; } List<Item> items =
     * db.queryAndCollect("SELECT DISTINCT ON (serial_number) * FROM Item WHERE
     * deleted = 'false'" + " AND type = 'foodstuff'" + " AND uuid LIKE ? " +
     * "OR serial_number LIKE ? " + "OR name LIKE ? " + "OR type LIKE ? " + "OR
     * location LIKE ? LIMIT 10", rs -> { return new Item(rs.getString("uuid"),
     * rs.getString("name"), rs.getString("serial_number"),
     * rs.getString("location"), rs.getTimestamp("created_on"),
     * rs.getTimestamp("expiration"),
     * tagDao.findAllByIdentifier(rs.getString("uuid")),
     * tagDao.findAllByIdentifier(rs.getString("serial_number")),
     * Type.parseType(rs.getString("type"))); }, objs);
     *
     * return items; }
     *
     */
//    public List<Item> search(String... searchWords) throws SQLException {
//        if (searchWords.length == 0) {
//            return null;
//        }
//
//        String sql = "SELECT * FROM "
//                + "(SELECT item.*, "
//                + "to_tsvector(item.uuid) || "
//                + "to_tsvector(item.name) || "
//                + "to_tsvector(item.serial_number) || "
//                + "to_tsvector(item.location) || "
//                + "to_tsvector(item.type) || "
//                + "to_tsvector(coalesce((string_agg(tag.value, '')), '')) "
//                + "AS document "
//                + "FROM Item LEFT JOIN Tag ON tag.identifier = item.serial_number "
//                + "WHERE item.deleted = 'false' GROUP BY item.uuid) i_search "
//                + "WHERE i_search.document @@ to_tsquery(?)";
//
//        for (int i = 0; i < searchWords.length - 1; i++) {
//            sql += " AND i_search.document @@ to_tsquery(?)";
//        }
//
//        List<Item> items = db.queryAndCollect(sql, rs -> {
//            return new Item(rs.getString("uuid"),
//                    rs.getString("name"),
//                    rs.getString("serial_number"),
//                    rs.getString("location"),
//                    rs.getTimestamp("created_on"),
//                    rs.getTimestamp("expiration"),
//                    tagDao.findAllByIdentifier(rs.getString("uuid")),
//                    tagDao.findAllByIdentifier(rs.getString("serial_number")),
//                    Type.getType(rs.getString("type")));
//        }, searchWords);
//
//        return items;
//    }
}
