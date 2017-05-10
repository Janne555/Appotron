/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.MealComponent;
import storables.User;

public class MealComponentDao {

    private Database db;
    private FoodstuffDao foodDao;

    public MealComponentDao(Database db) {
        this.db = db;
        this.foodDao = new FoodstuffDao(db);
    }

    public MealComponent store(MealComponent mealComponent) throws SQLException {
        int update = db.update("INSERT INTO MealComponent(meal_id, globalreference_id, mass) VALUES(?,?,?)", true,
                mealComponent.getMealId(),
                mealComponent.getFoodstuff().getGlobalReferenceId(),
                mealComponent.getMass());
        mealComponent.setId(update);
        return mealComponent;
    }

    public List<MealComponent> findByMealId(int mealId) throws SQLException {
        List<MealComponent> queryAndCollect = db.queryAndCollect("SELECT * FROM MealComponent WHERE meal_id = ?", rs -> {
            return new MealComponent(rs.getInt("id"),
                    rs.getInt("meal_id"),
                    rs.getFloat("mass"),
                    foodDao.findOne(rs.getInt("globalreference_id")));
        }, mealId);
        return queryAndCollect;
    }

    public void deleteAllByMealId(int mealId) throws SQLException {
        db.update("DELETE FROM mealcomponent WHERE meal_id = ?", false, mealId);
    }

    public void delete(User user, int mealComponentId) throws SQLException {
        if (db.canDeleteComponent(user.getId(), mealComponentId)) {
            db.update("DELETE FROM mealcomponent WHERE mealcomponent.id = ?", false, mealComponentId);
        }
    }

    public void update(User user, MealComponent c) throws SQLException {
        if (db.canEditMeal(user.getId(), c.getMealId())) {
            db.update("UPDATE mealcomponent SET mass = ? WHERE mealcomponent.id = ?", false, c.getMass(), c.getId());
        }
    }

}
