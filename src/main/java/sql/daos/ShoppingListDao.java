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
import storables.ShoppingList;

/**
 *
 * @author Janne
 */
public class ShoppingListDao {
    private Database db;
    private ListItemDao liDao;

    public ShoppingListDao(Database db) {
        this.db = db;
        this.liDao = new ListItemDao(db);
    }
    
    public ShoppingList store(ShoppingList t) throws SQLException {
        int update = db.update("INSERT INTO ShoppingList(name, date, deleted) VALUES(?,?,?)", true,
                t.getName(), t.getDate(), false);
        t.setId(update);
        return t;
    }

    public ShoppingList findOne(int key) throws SQLException {
        List<ShoppingList> queryAndCollect = db.queryAndCollect("SELECT * FROM ShoppingList WHERE id = ?", rs -> {
            return new ShoppingList(Integer.parseInt(rs.getString("id")),
                    rs.getString("name"),
                    rs.getTimestamp("date"), 
                    liDao.findAllByShoppingListId(key));
        }, key);
        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    public void update(ShoppingList t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
