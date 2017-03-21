/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.ItemInfo;

/**
 *
 * @author Janne
 */
public class ItemInfoDao {

    private Database db;
    private ItemInfoTagDao ifDao;

    public ItemInfoDao(Database db) {
        this.db = db;
        this.ifDao = new ItemInfoTagDao(db);
    }

    public ItemInfo store(ItemInfo itemInfo) throws SQLException {
        int update = db.update("INSERT INTO ItemInfo(name, identifier, type) VALUES(?,?,?)", true,
                itemInfo.getName(), itemInfo.getIdentifier(), itemInfo.getType());
        itemInfo.setId(update);
        return itemInfo;
    }

    public ItemInfo findOne(int id) throws SQLException {
        List<ItemInfo> queryAndCollect = db.queryAndCollect("SELECT * FROM ItemInfo WHERE id = ?", rs -> {
            return new ItemInfo(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("identifier"), ifDao.findAll(id));
        }, id);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public ItemInfo findOne(String name, String identifier) throws SQLException {
        List<ItemInfo> queryAndCollect = db.queryAndCollect("SELECT * FROM ItemInfo WHERE name = ? AND identifier = ?", rs -> {
            return new ItemInfo(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("identifier"), ifDao.findAll(rs.getInt("id")));
        }, name, identifier);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }
    
    public List<ItemInfo> search(Object... searchWords) throws SQLException {
        String sql = "SELECT * FROM (SELECT *, "
                + "to_tsvector(name) || "
                + "to_tsvector(identifier) || "
                + "to_tsvector(type) AS document FROM ItemInfo) AS query "
                + "WHERE query.document @@ to_tsquery(?)";
        
        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND query.document @@ to_tsquery(?)";
        }
        
        return db.queryAndCollect(sql, rs -> {
            return new ItemInfo(rs.getInt("id"), rs.getString("name"), rs.getString("type"), rs.getString("identifier"), ifDao.findAll(rs.getInt("id")));
        }, searchWords);
    }
    
}
