/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import sql.db.Database;
import storables.Meal;
import storables.User;

public class MealDao {

    private Database db;
    private MealComponentDao mecDao;

    public MealDao(Database db) {
        this.db = db;
        this.mecDao = new MealComponentDao(db);
    }

    public Meal store(Meal meal) throws SQLException {
        int update = db.update("INSERT INTO Meal(users_id, date, deleted) VALUES(?,?,?)",
                meal.getUserId(),
                meal.getDate(),
                false);

        meal.setId(update);
        return meal;
    }

    public List<Meal> findAll(User user) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND users_id = ? ORDER BY date DESC", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getString("id")));
        }, user.getId());
    }

    public List<Meal> findAll(User user, int limit) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND users_id = ? ORDER BY date DESC LIMIT ?", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getString("id")));
        }, user.getId(), limit);
    }

    public List<Meal> findAll(User user, Timestamp from, Timestamp to) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND users_id = ? AND date > ? AND date < ? ORDER BY date DESC", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getString("id")));
        }, user.getId(), from, to);
    }

}
