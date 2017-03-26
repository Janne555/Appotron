/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Ingredient;

/**
 *
 * @author Janne
 */
public class IngredientDao {
    private Database db;
    private ItemInfoDao infoDao;
    private NutritionalInfoDao nutDao;

    public IngredientDao(Database db) {
        this.db = db;
        this.infoDao = new ItemInfoDao(db);
        this.nutDao = new NutritionalInfoDao(db);
    }
    
    public Ingredient store(Ingredient ingredient) throws SQLException {
        int update = db.update("INSERT INTO Ingredient(iteminfo_id, recipe_id, amount, unit) VALUES(?,?,?,?)", true,
                ingredient.getItemInfoId(), ingredient.getRecipeId(), ingredient.getAmount(), ingredient.getUnit());
        ingredient.setId(update);
        return ingredient;
    }
    
    public List<Ingredient> findAllByRecipeId(int recipeId) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Ingredient as i, Conversions as c WHERE i.recipe_id = ? AND i.iteminfo_id = c.iteminfo_id", rs -> {
            return new Ingredient(rs.getInt("i.id"),
                    rs.getInt("i.recipe_id"),
                    rs.getFloat("i.amount"),
                    rs.getString("i.unit"),
                    infoDao.findOne(rs.getInt("i.iteminfo_id")),
                    rs.getFloat("c.unitspergram"),
                    nutDao.findOneByItemInfoId(rs.getInt("i.iteminfo_id")));
        }, recipeId);
    }
}
