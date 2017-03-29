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
    private FoodstuffDao foodDao;

    public IngredientDao(Database db) {
        this.db = db;
        this.foodDao = new FoodstuffDao(db);
    }
    
    public Ingredient store(Ingredient ingredient) throws SQLException {
        int update = db.update("INSERT INTO ingredient(globalreference_id, recipe_id, mass) VALUES(?,?,?)", true,
                ingredient.getGlobalReferenceId(), ingredient.getRecipeId(), ingredient.getMass());
        ingredient.setId(update);
        return ingredient;
    }
    
    public List<Ingredient> findAllByRecipeId(int recipeId) throws SQLException {
        return db.queryAndCollect("SELECT * FROM ingredient WHERE ingredient.recipe_id = ?", rs -> {
            return new Ingredient(rs.getInt("id"),
                    rs.getInt("globalreference_id"), 
                    rs.getInt("recipe_id"), 
                    rs.getFloat("mass"), 
                    foodDao.findOne(rs.getInt("i.globalreference_id")));
        }, recipeId);
    }
}
