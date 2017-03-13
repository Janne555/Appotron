/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Meal;

public class MealDao {

    private Database db;
    private IngredientDao ingDao;

    public MealDao(Database db) {
        this.db = db;
        this.ingDao = new IngredientDao(db);
    }

    public void create(Meal meal) throws SQLException {
        db.update("INSERT INTO Meal(id, name, type, deleted) VALUES(?,?,?,?)", meal.getObjs());
    }
    
    public List<Meal> findAll() throws SQLException {
        List<Meal> queryAndCollect = db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false'", rs -> {
            return new Meal(rs.getString("id"), rs.getString("name"), rs.getString("type"), ingDao.findByMealId(rs.getString("id")).toArray());
        });
        
        return queryAndCollect;
    }
    
    public Meal findOne(String serial) throws SQLException {
        List<Meal> queryAndCollect = db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND id = ?", rs -> {
            return new Meal(rs.getString("id"), rs.getString("name"), rs.getString("type"), ingDao.findByMealId(rs.getString("id")).toArray());
        }, serial);
        
        if (queryAndCollect.isEmpty()) return null;
        
        return queryAndCollect.get(0);
    }
}
