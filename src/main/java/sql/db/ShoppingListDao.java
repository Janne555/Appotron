/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.SQLException;
import java.util.List;
import storables.ShoppingList;

/**
 *
 * @author Janne
 */
public class ShoppingListDao implements Dao<ShoppingList, String>{
    private Database db;
    private ListItemDao liDao;

    public ShoppingListDao(Database db) {
        this.db = db;
        this.liDao = new ListItemDao(db);
    }
    
    @Override
    public ShoppingList create(ShoppingList t) throws SQLException {
        db.update("INSERT INTO ShoppingList(name, created_on) VALUES(?,?)", t.getObjs());
        return t;
    }

    @Override
    public ShoppingList findOne(String key) throws SQLException {
        List<ShoppingList> queryAndCollect = db.queryAndCollect("SELECT * FROM ShoppingList WHERE id = ?", rs -> {
            return new ShoppingList(rs.getInt("id"),
                    rs.getString("name"),
                    new Timestamp(rs.getString("created_on")), 
                    liDao.findAllByShoppingList(key));
        }, key);
        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    @Override
    public List<ShoppingList> findAll() throws SQLException {
        List<ShoppingList> queryAndCollect = db.queryAndCollect("SELECT * FROM ShoppingList", rs -> {
            return new ShoppingList(rs.getInt("id"),
                    rs.getString("name"),
                    new Timestamp(rs.getString("created_on")), 
                    liDao.findAllByShoppingList(rs.getString("id")));
        });
        return queryAndCollect;
    }

    @Override
    public void update(ShoppingList t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
