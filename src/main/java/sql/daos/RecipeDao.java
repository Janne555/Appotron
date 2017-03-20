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
import storables.Recipe;

/**
 *
 * @author Janne
 */
public class RecipeDao {

    private Database db;
    private IngredientDao ingDao;

    public RecipeDao(Database db) {
        this.db = db;
        this.ingDao = new IngredientDao(db);
    }

    public Recipe store(Recipe recipe) throws SQLException {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingDao.store(ingredient);
        }
        int update = db.update("INSERT INTO recipe(name, directions, description, type, deleted) VALUES(?,?,?,?,?)",
                recipe.getName(), recipe.getDirections(), recipe.getDescription(), recipe.getType(), false);
        recipe.setId(update);
        return recipe;
    }

    public List<Recipe> search(Object[] searchWords) throws SQLException {
        String sql = "SELECT r.*, "
                + "to_tsvector(r.name) || "
                + "to_tsvector(r.type) || "
                + "to_tsvector(i.name) AS document "
                + "FROM recipe AS r JOIN ingredient AS g ON g.recipe_id = r.id JOIN iteminfo AS i ON g.iteminfo_id = i.id WHERE deleted = 'false'";

        for (int i = 0; i < searchWords.length; i++) {
            sql += " AND document @@ to_tsquery(?)";
        }
        return db.queryAndCollect(sql, rs -> {
            return new Recipe(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("directions"),
                    rs.getString("description"),
                    rs.getString("type"), 
                    ingDao.findAllByRecipeId(rs.getInt("id")));
        });
    }
}
