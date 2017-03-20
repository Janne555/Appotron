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
                + "FROM (SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE i.deleted = 'false' AND i.id = a.item_id AND u.id = a.users_id AND u.id = ?) AS item "
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
        String sql = "SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE AND i.deleted = 'false' AND i.id = a.item_id AND u.id = a.users_id AND u.id = ?";
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

    public List<Item> getExpiring(User user, int limit) throws SQLException {
        String sql = "SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE AND i.deleted = 'false' AND i.id = a.item_id AND u.id = a.users_id AND u.id = ? AND i.expiration IS NOT NULL ORDER BY i.expiration ASC LIMIT ?";
        return db.queryAndCollect(sql, rs -> {
            return new Item(rs.getInt("i.id"),
                    rs.getString("i.location"),
                    rs.getTimestamp("i.date"),
                    rs.getTimestamp("i.expiration"),
                    tagDao.findAll(rs.getInt("i.id")),
                    infoDao.findOne(rs.getInt("i.iteminfo_id")));
        }, user.getId(), limit);
    }

    public List<String> getLocations(User user) throws SQLException {
        String sql = "SELECT i.* FROM Item as i, AccessControl as a, Users as u WHERE AND i.deleted = 'false' AND i.id = a.item_id AND u.id = a.users_id AND u.id = ? ORDER BY location ASC";
        return db.queryAndCollect(sql, rs -> {
            return rs.getString("location");
        }, user.getId());
    }
}
