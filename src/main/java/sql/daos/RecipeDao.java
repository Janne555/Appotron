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
        if (recipe.getIngredients() != null) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingDao.store(ingredient);
            }
        }
        int update = db.update("INSERT INTO recipe(name, directions, description, type, totalmass, date, deleted) VALUES(?,?,?,?,?,?,?)", true,
                recipe.getName(), recipe.getDirections(), recipe.getDescription(), recipe.getType(), recipe.getTotalMass(), recipe.getDate(), false);
        recipe.setId(update);
        return recipe;
    }

    public List<Recipe> search(Object[] searchWords) throws SQLException {
        String sql = "SELECT DISTINCT ON (id) * FROM (SELECT r.id as id, r.name as name, r.directions as directions, r.description as description, r.type as type, r.totalmass as totalmass, r.date as date, to_tsvector(r.name) || to_tsvector(r.type) || to_tsvector(g.name) AS document FROM recipe AS r JOIN (SELECT name, ingredient.recipe_id AS recipe_id FROM ingredient, globalreference WHERE ingredient.globalreference_id = globalreference.id) AS g ON g.recipe_id = r.id WHERE r.deleted = 'false') as mainquery WHERE mainquery.document @@ to_tsquery(?)";

        for (int i = 1; i < searchWords.length; i++) {
            sql += " AND mainquery.document @@ to_tsquery(?)";
        }

        return db.queryAndCollect(sql, rs -> {
            return new Recipe(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("directions"),
                    rs.getString("description"),
                    rs.getString("type"),
                    rs.getFloat("totalmass"),
                    rs.getTimestamp("date"),
                    ingDao.findAllByRecipeId(rs.getInt("id")));
        });
    }

    public Recipe findOne(int id) throws SQLException {
        List<Recipe> queryAndCollect = db.queryAndCollect("SELECT * FROM Recipe WHERE id = ?", rs -> {
            return new Recipe(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("directions"),
                    rs.getString("description"),
                    rs.getString("type"),
                    rs.getFloat("totalmass"),
                    rs.getTimestamp("date"),
                    ingDao.findAllByRecipeId(rs.getInt("id")));
        }, id);

        if (queryAndCollect.isEmpty()) {
            return null;
        }
        return queryAndCollect.get(0);
    }

    public List<Recipe> findAll() throws SQLException {
        return db.queryAndCollect("SELECT * FROM Recipe", rs -> {
            return new Recipe(rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("directions"),
                    rs.getString("description"),
                    rs.getString("type"),
                    rs.getFloat("totalmass"),
                    rs.getTimestamp("date"),
                    ingDao.findAllByRecipeId(rs.getInt("id")));
        });
    }
}
