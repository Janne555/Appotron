/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.db.Database;
import storables.Ingredient;

public class IngredientDao {

    private Database db;
    private ItemDao iDao;
    private NutritionalInfoDao nuDao;

    public IngredientDao(Database db) {
        this.db = db;
        this.iDao = new ItemDao(db);
        this.nuDao = new NutritionalInfoDao(db);
    }

    public void createIngredient(Ingredient ingredient) throws SQLException {
        db.update("INSERT INTO Ingredient(meal_id, item_identifier, percentage, deleted) VALUES(?,?,?,?)", ingredient.getObjs());
    }
    
    public List<Ingredient> findByMealId(String mealId) throws SQLException {
        List<Ingredient> queryAndCollect = db.queryAndCollect("SELECT * FROM Ingredient WHERE deleted = 'false' AND meal_id = ?", rs -> {
            return new Ingredient(rs.getString("meal_id"),
                    rs.getString("item_identifier"),
                    rs.getFloat("percentage"),
                    iDao.findOneBySerial(rs.getString("item_identifier")),
                    nuDao.findOne(rs.getString("item_identifier")));
        },mealId);
        return queryAndCollect;
    }
}
