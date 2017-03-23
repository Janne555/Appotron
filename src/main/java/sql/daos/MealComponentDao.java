/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.db.Database;
import storables.ItemInfo;
import storables.MealComponent;

public class MealComponentDao {

    private Database db;
    private ItemInfoDao infodao;
    private NutritionalInfoDao nuDao;

    public MealComponentDao(Database db) {
        this.db = db;
        this.infodao = new ItemInfoDao(db);
        this.nuDao = new NutritionalInfoDao(db);
    }

    public MealComponent store(MealComponent mealComponent) throws SQLException {
        int update = db.update("INSERT INTO MealComponent(meal_id, iteminfo_id, mass) VALUES(?,?,?)", true,
                mealComponent.getMealId(),
                mealComponent.getItemId(),
                mealComponent.getMass());
        mealComponent.setId(update);
        return mealComponent;
    }
    
    public List<MealComponent> findByMealId(int mealId) throws SQLException {
        List<MealComponent> queryAndCollect = db.queryAndCollect("SELECT * FROM MealComponent WHERE meal_id = ?", rs -> {
            return new MealComponent(rs.getInt("id"),
                    rs.getInt("meal_id"),
                    rs.getFloat("mass"),
                    infodao.findOne(rs.getInt("iteminfo_id")),
                    nuDao.findOneByItemInfoId(rs.getInt("iteminfo_id")));
        },mealId);
        return queryAndCollect;
    }
}
