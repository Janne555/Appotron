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
        return db.queryAndCollect("select (calories * mass) as calories, (carbohydrate * mass) as carbohydrate, (fat * mass) as fat, (protein * mass) as protein, i.recipe_id as recipeid, i.globalreference_id as globalreferenceid, mass, i.id as ingredientid from ingredient i, foodstuffmeta f where i.globalreference_id = f.globalreference_id AND i.recipe_id = ?", rs -> {
            return new Ingredient(rs.getInt("ingredientid"),
                    rs.getInt("globalreferenceid"),
                    rs.getInt("recipeid"),
                    rs.getFloat("mass"),
                    rs.getFloat("calories"),
                    rs.getFloat("carbohydrate"),
                    rs.getFloat("fat"),
                    rs.getFloat("protein"),
                    foodDao.findOne(rs.getInt("globalreferenceid")));
        }, recipeId);
    }
}
