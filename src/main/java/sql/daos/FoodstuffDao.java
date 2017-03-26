/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import sql.db.Database;
import storables.Foodstuff;

/**
 *
 * @author janne
 */
public class FoodstuffDao {

    private Database db;

    public FoodstuffDao(Database db) {
        this.db = db;
    }

    public Foodstuff store(Foodstuff foodstuff) throws SQLException {
        /**
         * tries to add a row into globalreference
         *
         * failing should mean that there is already an entry for the reference
         */
        try {

            int update = db.update("INSERT INTO globalreference(name, identifier, type) VALUES(?, ?, ?)", true,
                    foodstuff.getName(), foodstuff.getIdentifier(), "foodstuff");
            foodstuff.setGlobalReference(update);

        } catch (SQLException e) {
            if (e.getErrorCode() == 0) {

            }
            throw e;
        }

        /**
         * tries to add a row into foodstuffmetagetIdentifier
         *
         * failing should mean that there is already an entry for the
         * corresponding global reference
         */
        try {
            int update = db.update("INSERT INTO foodstuffmeta(globalreference_id, producer, calories, carbohydrate, fat, protein) VALUES(?, ?, ?, ?, ?, ?)", true,
                    foodstuff.getGlobalReference(), foodstuff.getProducer(), foodstuff.getCalories(), foodstuff.getCarbohydrate(), foodstuff.getFat(), foodstuff.getProtein());
            foodstuff.setFoodstuffMeta(update);

        } catch (SQLException e) {
            throw e;
        }

    }
}
