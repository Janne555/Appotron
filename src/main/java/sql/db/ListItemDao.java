/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.SQLException;
import java.util.List;
import storables.ListItem;

public class ListItemDao<T, K> implements Dao<ListItem, String> {

    private Database db;

    public ListItemDao(Database db) {
        this.db = db;
    }

    @Override
    public ListItem create(ListItem t) throws SQLException {
        db.update("INSERT INTO ListItem(shopping_list, serial_number, amount) VALUES(?,?,?)", t.getObjs());
        return t;
    }

    @Override
    public ListItem findOne(String key) throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT * FROM ListItem WHERE id = ?", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("serial_number"),
                    rs.getInt("amount"));
        }, key);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    @Override
    public List<ListItem> findAll() throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT * FROM ListItem", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("serial_number"),
                    rs.getInt("amount"));
        });
        return queryAndCollect;
    }
    
    public List<ListItem> findAllByShoppingList(String shoppingListId) throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT * FROM ListItem WHERE shopping_list = ?", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("serial_number"),
                    rs.getInt("amount"));
        }, shoppingListId);
        return queryAndCollect;
    }

    @Override
    public void update(ListItem t) throws SQLException {
        db.update("UPDATE ListItem SET shopping_list = ?, serial_number = ?, amount = ? WHERE id = ?", t.getObjsId());
    }

    @Override
    public void delete(String key) throws SQLException {
        db.update("DELETE FROM ListItem WHERE id = ?", key);
    }

}
