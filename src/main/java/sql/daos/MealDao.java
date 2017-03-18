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
    private MealComponentDao ingDao;

    public MealDao(Database db) {
        this.db = db;
        this.ingDao = new MealComponentDao(db);
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

    public List<Meal> search(String... searchWords) throws SQLException {
        if (searchWords.length == 0) {
            return null;
        }

        String sql = "SELECT * FROM "
                + "(SELECT mealname AS name, mealtype AS type, mealid AS id, "
                + "to_tsvector(mealname) || "
                + "to_tsvector(mealtype) || "
                + "to_tsvector(mealid) || "
                + "to_tsvector(ingredientname) "
                + "AS document "
                + "FROM (SELECT "
                + "meal.name AS mealname, "
                + "meal.type AS mealtype, "
                + "meal.id AS mealid, "
                + "itemname AS ingredientname "
                + "FROM meal LEFT JOIN("
                + "SELECT item.name AS itemname, ingredient.* FROM ingredient LEFT JOIN item ON item.serial_number = ingredient.item_identifier) AS firstquery "
                + "ON meal.id = firstquery.meal_id) AS secondquery) AS m_search "
                + "WHERE m_search.document @@ to_tsquery(?);";

        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND i_search.document @@ to_tsquery(?)";
        }

        List<Meal> queryAndCollect = db.queryAndCollect(sql, rs -> {
            return new Meal(rs.getString("id"), rs.getString("name"), rs.getString("type"), ingDao.findByMealId(rs.getString("id")).toArray());
        }, searchWords);

        return queryAndCollect;
    }

    public Meal findOne(String serial) throws SQLException {
        List<Meal> queryAndCollect = db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND id = ?", rs -> {
            return new Meal(rs.getString("id"), rs.getString("name"), rs.getString("type"), ingDao.findByMealId(rs.getString("id")).toArray());
        }, serial);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }
}
