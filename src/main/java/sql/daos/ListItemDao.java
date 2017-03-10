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

    public ListItemDao(Database db) {
        this.db = db;
    }

    public ListItem create(ListItem t) throws SQLException {
        db.update("INSERT INTO ListItem(shopping_list, serial_number, amount) VALUES(?,?,?)", t.getObjs());
        return t;
    }

    public ListItem findOne(int key) throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT ListItem.id AS id, "
                + "ListItem.shopping_list AS shopping_list, "
                + "ListItem.serial_number AS sernum, "
                + "ListItem.amount AS amount, "
                + "Item.name AS name "
                + "FROM ListItem "
                + "LEFT JOIN Item ON ListItem.serial_number = Item.serial_number WHERE id = ?", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("sernum"),
                    rs.getString("name"),
                    rs.getInt("amount"));
        }, key);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    public List<ListItem> findAll() throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT ListItem.id AS id, "
                + "ListItem.shopping_list AS shopping_list, "
                + "ListItem.serial_number AS sernum, "
                + "ListItem.amount AS amount, "
                + "Item.name AS name "
                + "FROM ListItem "
                + "LEFT JOIN Item ON ListItem.serial_number = Item.serial_number", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("sernum"),
                    rs.getString("name"),
                    rs.getInt("amount"));
        });
        return queryAndCollect;
    }
    
    public List<ListItem> findAllByShoppingList(int shoppingListId) throws SQLException {
        List<ListItem> queryAndCollect = db.queryAndCollect("SELECT ListItem.id AS id, "
                + "ListItem.shopping_list AS shopping_list, "
                + "ListItem.serial_number AS sernum, "
                + "ListItem.amount AS amount, "
                + "Item.name AS name "
                + "FROM ListItem "
                + "LEFT JOIN Item ON ListItem.serial_number = Item.serial_number WHERE shopping_list = ?", rs -> {
            return new ListItem(rs.getInt("id"),
                    rs.getInt("shopping_list"),
                    rs.getString("sernum"),
                    rs.getString("name"),
                    rs.getInt("amount"));
        }, shoppingListId);
        return queryAndCollect;
    }

    public void update(ListItem t) throws SQLException {
        db.update("UPDATE ListItem SET shopping_list = ?, serial_number = ?, amount = ? WHERE id = ?", t.getObjsId());
    }

    public void delete(String key) throws SQLException {
        db.update("DELETE FROM ListItem WHERE id = ?", key);
    }

}
