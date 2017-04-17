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
import storables.MealComponent;
import storables.User;
import util.Container;

public class MealDao {

    private Database db;
    private MealComponentDao mecDao;

    public MealDao(Database db) {
        this.db = db;
        this.mecDao = new MealComponentDao(db);
    }

    public Meal store(Meal meal) throws SQLException {
        int update = db.update("INSERT INTO Meal(person_identifier, date, deleted) VALUES(?,?,?)", true,
                meal.getUserId(),
                meal.getDate(),
                false);

        meal.setId(update);
        return meal;
    }

    public List<Meal> findAll(User user) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? ORDER BY date DESC", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId());
    }

    public List<Meal> findAll(User user, int limit) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? ORDER BY date DESC LIMIT ?", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId(), limit);
    }

    public List<Meal> findAll(User user, Timestamp from, Timestamp to) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? AND date >= ? AND date <= ? ORDER BY date DESC", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId(), from, to);
    }

    public int count(User user, Timestamp from, Timestamp to) throws SQLException {
        List<Integer> queryAndCollect = db.queryAndCollect("SELECT COUNT(id) AS meals_number FROM Meal WHERE deleted = 'false' AND person_identifier = ? AND date >= ? AND date <= ?", rs -> {
            return rs.getInt("meals_number");
        }, user.getId(), from, to);

        return queryAndCollect.get(0);
    }

    public List<Meal> findAll(User user, Timestamp from, Timestamp to, int offset, int limit) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? AND date >= ? AND date <= ? ORDER BY date DESC OFFSET ? LIMIT ?", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId(), from, to, offset, limit);
    }

    public List<Meal> findTodays(User user) throws SQLException {
        return db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? AND date >= CURRENT_DATE AND date <= CURRENT_DATE + interval '1 day' ORDER BY date DESC", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId());
    }

    public Meal findOne(User user, int id) throws SQLException {
        List<Meal> queryAndCollect = db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? AND id = ?", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId(), id);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public void delete(User user, int id) throws SQLException {
        mecDao.deleteAllByMealId(id);
        db.update("DELETE FROM meal WHERE id = ? AND person_identifier = ?", false, id, user.getId());
    }

    public List<Container> dailyTotals(User user, Timestamp from, Timestamp to) throws SQLException {
        String sql = "SELECT SUM(totalcalories) as totalcalories, SUM(totalcarbohydrate) as totalcarbohydrate, SUM(totalfat) as totalfat, SUM(totalprotein) as totalprotein, DATE_TRUNC('day', meal.date) as truncdate FROM (select *, (mc.mass * fm.calories) as totalCalories, (mc.mass * fm.carbohydrate) as totalCarbohydrate, (mc.mass * fm.protein) as totalProtein, (mc.mass * fm.fat) as totalFat from mealcomponent mc, foodstuffmeta fm WHERE mc.globalreference_id = fm.globalreference_id) as foo, person, meal WHERE meal.id = meal_id AND meal.person_identifier = person.identifier AND person.identifier = ? AND meal.date >= ? AND meal.date <= ? GROUP BY truncdate ORDER BY truncdate ASC";

        return db.queryAndCollect(sql, rs -> {
            return new Container(rs.getTimestamp("truncdate").toLocalDateTime().toLocalDate(), rs.getFloat("totalcarbohydrate"), rs.getFloat("totalfat"), rs.getFloat("totalprotein"), rs.getFloat("totalcalories"));
        }, user.getId(), from, to);
    }

    public Meal findLatest(User user) throws SQLException {
        List<Meal> queryAndCollect = db.queryAndCollect("SELECT * FROM Meal WHERE deleted = 'false' AND person_identifier = ? ORDER BY date DESC LIMIT 1", rs -> {
            return new Meal(rs.getInt("id"), user, rs.getTimestamp("date"), mecDao.findByMealId(rs.getInt("id")));
        }, user.getId());
        
        if (queryAndCollect.isEmpty()) {
            return null;
        }
        
        return queryAndCollect.get(0);
    }

    public void update(User user, Meal newMeal) throws SQLException {
        if (db.canEditMeal(user.getId(), newMeal.getId())) {
            db.update("UPDATE meal SET date = ?", false, newMeal.getDate());
            findOne(user, newMeal.getId()).getComponents();
            for (MealComponent c : newMeal.getComponents()) {
                if (c.getId() != 0) {
                    mecDao.update(c);
                }
            }
        }
    }
}
