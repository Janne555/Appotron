/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Dao;
import sql.db.Database;
import storables.ListItem;

public class ListItemDao {

    private Database db;
    private ItemInfoDao infoDao;

    public ListItemDao(Database db) {
        this.db = db;
        this.infoDao = new ItemInfoDao(db);
    }

    public ListItem store(ListItem t) throws SQLException {
        int update = db.update("INSERT INTO ListItem(iteminfo_id, shoppinglist_id, amount) VALUES(?,?,?)",
                t.getIteminfoId(),
                t.getShoppingListId());
        t.setId(update);
        return t;
    }

    public List<ListItem> findAllByShoppingListId(int shoppingListId) throws SQLException {
        return db.queryAndCollect("SELECT * FROM ListItem WHERE shoppinglist_id = ?", rs -> {
            return new ListItem(rs.getInt("id"), rs.getInt("shoppinglist_id"), infoDao.findOne(rs.getInt("iteminfo_id")), rs.getInt("amount"));
        });
    }
}
