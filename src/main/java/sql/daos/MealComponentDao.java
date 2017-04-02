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
        },mealId);
        return queryAndCollect;
    }
    
    public void deleteAllByMealId(int mealId) throws SQLException {
        db.update("DELETE FROM mealcomponent WHERE meal_id = ?", false, mealId);
    }
}
