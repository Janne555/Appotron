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

    public IngredientDao(Database db) {
        this.db = db;
        this.infoDao = new ItemInfoDao(db);
    }
    
    public Ingredient store(Ingredient ingredient) throws SQLException {
        int update = db.update("INSERT INTO Ingredient(iteminfo_id, recipe_id, amount, unit, ordernumber) VALUES(?,?,?,?)",
                ingredient.getItemInfoId(), ingredient.getRecipeId(), ingredient.getAmount(), ingredient.getUnit(), ingredient.getOrderNumber());
        ingredient.setId(update);
        return ingredient;
    }
    
    public List<Ingredient> findAllByRecipeId(int recipeId) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Ingredient WHERE recipe_id = ? ORDER BY ordernumber ASC", rs -> {
            return new Ingredient(rs.getInt("id"), rs.getInt("recipe_id"), rs.getFloat("amount"), rs.getString("unit"), infoDao.findOne(rs.getInt("iteminfo_id")), rs.getInt("ordernumber"));
        });
    }
}
