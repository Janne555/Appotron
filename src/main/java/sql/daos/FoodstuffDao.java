/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Foodstuff;
import storables.Permission;
import storables.User;

/**
 *
 * @author janne
 */
public class FoodstuffDao {

    private Database db;
    private PermissionDao perDao;

    public FoodstuffDao(Database db) {
        this.db = db;
        this.perDao = new PermissionDao(db);
    }

    public Foodstuff store(Foodstuff foodstuff, User user) throws SQLException {
        List<Integer> globalReferenceIdList = db.queryAndCollect("SELECT id FROM globalreference WHERE name = ? AND identifier = ?", rs -> {
            return rs.getInt("id");
        }, foodstuff.getName(), foodstuff.getIdentifier());

        if (globalReferenceIdList.isEmpty()) {
            int update = db.update("INSERT INTO globalreference(name, identifier, type) VALUES(?, ?, ?)", true,
                    foodstuff.getName(), foodstuff.getIdentifier(), "foodstuff");
            foodstuff.setGlobalReferenceId(update);
        } else if (globalReferenceIdList.size() > 1) {
            throw new VerifyError("found more than one row with name: " + foodstuff.getName() + " and identifier: " + foodstuff.getIdentifier());
        } else {
            foodstuff.setGlobalReferenceId(globalReferenceIdList.get(0));
        }

        List<Integer> foodstuffMetaIdList = db.queryAndCollect("SELECT id FROM foodstuffmeta WHERE globalreference_id = ?", rs -> {
            return rs.getInt("id");
        }, foodstuff.getGlobalReferenceId());

        if (foodstuffMetaIdList.isEmpty()) {
            int update = db.update("INSERT INTO foodstuffmeta(globalreference_id, producer, calories, carbohydrate, fat, protein) VALUES(?, ?, ?, ?, ?, ?)", true,
                    foodstuff.getGlobalReferenceId(), foodstuff.getProducer(), foodstuff.getCalories(), foodstuff.getCarbohydrate(), foodstuff.getFat(), foodstuff.getProtein());
            foodstuff.setFoodstuffMetaId(update);
        } else if (foodstuffMetaIdList.size() > 1) {
            throw new VerifyError("found more than one row with global reference id: " + foodstuff.getGlobalReferenceId());
        } else {
            foodstuff.setId(foodstuffMetaIdList.get(0));
        }

        int update = db.update("INSERT INTO Item(globalreference_id, location, date, expiration, deleted) VALUES(?,?,?,?,?)", true,
                foodstuff.getGlobalReferenceId(), foodstuff.getLocation(), foodstuff.getDate(), foodstuff.getExpiration(), false);
        foodstuff.setId(update);

        perDao.store(new Permission(0, user.getId(), foodstuff.getId(), true, true));

        return foodstuff;
    }

    public List<Foodstuff> search(User user, Object... searchWords) throws SQLException {
        Object[] terms = new Object[searchWords.length + 1];
        terms[0] = user.getId();
        for (int i = 1; i < terms.length; i++) {
            terms[i] = searchWords[i - 1];
        }

        String sql = "SELECT DISTINCT ON (id) * FROM (SELECT "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "g.name as name, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "i.location as location, "
                + "i.date as date, "
                + "i.expiration as expiration, "
                + "g.id as globalreferenceid, "
                + "i.id as id, "
                + "f.id as foodstuffmetaid, "
                + "to_tsvector(g.identifier) || "
                + "to_tsvector(g.type) || "
                + "to_tsvector(f.producer) || "
                + "to_tsvector(i.location) as document "
                + "FROM globalreference as g, item as i, foodstuffmeta as f, permission as p "
                + "WHERE g.id = i.globalreference_id "
                + "AND f.globalreference_id = g.id "
                + "AND p.item_id = i.id "
                + "AND i.deleted = 'false' "
                + "AND p.person_identifier = ?) as mainquery "
                + "WHERE mainquery.document @@ to_tsquery(?)";

        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND mainquery.document @@ to_tsquery(?)";
        }

        List<Foodstuff> items = db.queryAndCollect(sql, rs -> {
            return new Foodstuff(rs.getString("name"),
                    rs.getString("identifier"),
                    rs.getString("producer"),
                    rs.getString("location"),
                    rs.getFloat("calories"),
                    rs.getFloat("carbohydrate"),
                    rs.getFloat("fat"),
                    rs.getFloat("protein"),
                    rs.getInt("globalreferenceid"),
                    rs.getInt("foodstuffmetaid"),
                    rs.getInt("id"),
                    rs.getTimestamp("expiration"),
                    rs.getTimestamp("date"));
        }, terms);

        return items;
    }

    public List<Foodstuff> searchGlobal(Object... searchWords) throws SQLException {
        String sql = "SELECT * FROM (SELECT "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "g.name as name, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "g.id as globalreferenceid, "
                + "f.id as foodstuffmetaid, "
                + "to_tsvector(g.identifier) || "
                + "to_tsvector(g.type) || "
                + "to_tsvector(f.producer) || "
                + "to_tsvector(g.name) as document "
                + "FROM globalreference as g, foodstuffmeta as f "
                + "WHERE f.globalreference_id = g.id) as mainquery "
                + "WHERE mainquery.document @@ to_tsquery(?)";

        for (int i = 0; i < searchWords.length - 1; i++) {
            sql += " AND mainquery.document @@ to_tsquery(?)";
        }

        List<Foodstuff> items = db.queryAndCollect(sql, rs -> {
            return new Foodstuff(rs.getString("name"),
                    rs.getString("identifier"),
                    rs.getString("producer"),
                    null,
                    rs.getFloat("calories"),
                    rs.getFloat("carbohydrate"),
                    rs.getFloat("fat"),
                    rs.getFloat("protein"),
                    rs.getInt("globalreferenceid"),
                    rs.getInt("foodstuffmetaid"),
                    0,
                    null,
                    null);
        }, searchWords);

        return items;
    }

    public List<Foodstuff> findAll(User user) throws SQLException {
        return db.queryAndCollect("SELECT g.name as name, "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "i.location as location, "
                + "i.date as date, "
                + "i.expiration as expiration, "
                + "g.id as globalreferenceid, "
                + "i.id as id, "
                + "f.id as foodstuffmetaid "
                + "FROM globalreference as g, item as i, foodstuffmeta as f, permission as p "
                + "WHERE g.id = i.globalreference_id "
                + "AND f.globalreference_id = g.id "
                + "AND p.item_id = i.id "
                + "AND i.deleted = 'false' "
                + "AND p.person_identifier = ? ", rs -> {
                    return new Foodstuff(rs.getString("name"),
                            rs.getString("identifier"),
                            rs.getString("producer"),
                            rs.getString("location"),
                            rs.getInt("calories"),
                            rs.getInt("carbohydrate"),
                            rs.getInt("fat"),
                            rs.getInt("protein"),
                            rs.getInt("globalreferenceid"),
                            rs.getInt("foodstuffmetaid"),
                            rs.getInt("id"),
                            rs.getTimestamp("expiration"),
                            rs.getTimestamp("date"));
                }, user.getId());
    }

    public Foodstuff findOne(User user, int id) throws SQLException {
        List<Foodstuff> queryAndCollect = db.queryAndCollect("SELECT g.name as name, "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "i.location as location, "
                + "i.date as date, "
                + "i.expiration as expiration, "
                + "g.id as globalreferenceid, "
                + "i.id as id, "
                + "f.id as foodstuffmetaid "
                + "FROM globalreference as g, item as i, foodstuffmeta as f, permission as p "
                + "WHERE g.id = i.globalreference_id "
                + "AND f.globalreference_id = g.id "
                + "AND p.item_id = i.id "
                + "AND i.deleted = 'false'"
                + "AND p.person_identifier = ? AND i.id = ?", rs -> {
                    return new Foodstuff(rs.getString("name"),
                            rs.getString("identifier"),
                            rs.getString("producer"),
                            rs.getString("location"),
                            rs.getFloat("calories"),
                            rs.getFloat("carbohydrate"),
                            rs.getFloat("fat"),
                            rs.getFloat("protein"),
                            rs.getInt("globalreferenceid"),
                            rs.getInt("foodstuffmetaid"),
                            rs.getInt("id"),
                            rs.getTimestamp("expiration"),
                            rs.getTimestamp("date"));
                }, user.getId(), id);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public Foodstuff findOne(int id) throws SQLException {
        List<Foodstuff> queryAndCollect = db.queryAndCollect("SELECT g.name as name, "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "g.id as globalreferenceid, "
                + "f.id as foodstuffmetaid "
                + "FROM globalreference as g, foodstuffmeta as f "
                + "WHERE f.globalreference_id = g.id "
                + "AND g.id = ?", rs -> {
                    return new Foodstuff(rs.getString("name"),
                            rs.getString("identifier"),
                            rs.getString("producer"),
                            null,
                            rs.getFloat("calories"),
                            rs.getFloat("carbohydrate"),
                            rs.getFloat("fat"),
                            rs.getFloat("protein"),
                            rs.getInt("globalreferenceid"),
                            rs.getInt("foodstuffmetaid"),
                            0,
                            null,
                            null);
                }, id);
        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public List<String> getLocations(User user) throws SQLException {
        return db.queryAndCollect("SELECT DISTINCT ON (location) location "
                + "FROM item as i, permission as p "
                + "WHERE p.item_id = i.id "
                + "AND i.deleted = 'false' "
                + "AND p.person_identifier = ? ", rs -> {
                    return rs.getString("location");
                }, user.getId());
    }

    public List<Foodstuff> getExpiring(User user, int limit) throws SQLException {
        return db.queryAndCollect("SELECT g.name as name, "
                + "g.identifier as identifier, "
                + "g.type as type, "
                + "f.producer as producer, "
                + "f.calories as calories, "
                + "f.carbohydrate as carbohydrate, "
                + "f.fat as fat, "
                + "f.protein as protein, "
                + "i.location as location, "
                + "i.date as date, "
                + "i.expiration as expiration, "
                + "g.id as globalreferenceid, "
                + "i.id as id, "
                + "f.id as foodstuffmetaid "
                + "FROM globalreference as g, item as i, foodstuffmeta as f, permission as p "
                + "WHERE g.id = i.globalreference_id "
                + "AND f.globalreference_id = g.id "
                + "AND p.item_id = i.id "
                + "AND i.deleted = 'false' "
                + "AND p.person_identifier = ? "
                + "AND i.expiration > CURRENT_DATE "
                + "ORDER BY i.expiration ASC LIMIT ?", rs -> {
                    return new Foodstuff(rs.getString("name"),
                            rs.getString("identifier"),
                            rs.getString("producer"),
                            rs.getString("location"),
                            rs.getFloat("calories"),
                            rs.getFloat("carbohydrate"),
                            rs.getFloat("fat"),
                            rs.getFloat("protein"),
                            rs.getInt("globalreferenceid"),
                            rs.getInt("foodstuffmetaid"),
                            rs.getInt("id"),
                            rs.getTimestamp("expiration"),
                            rs.getTimestamp("date"));
                }, user.getId(), limit);
    }

    public void delete(User user, int id) throws SQLException {
        if (db.canDelete(user.getId(), id)) {
            db.update("UPDATE item SET deleted = 'true' WHERE item.id = ?", false, id);
        } else {
            throw new SecurityException("action is not authorized");
        }
    }

    public void edit(User user, Foodstuff foodstuff) throws SQLException {
        if (db.canEdit(user.getId(), foodstuff.getId())) {
            db.update("UPDATE globalreference SET name = ?, identifier = ?, type = ? WHERE item.id = ?", false,
                    foodstuff.getName(), foodstuff.getIdentifier(), "foodstuff", foodstuff.getId());

            db.update("UPDATE foodstuffmeta SET producer = ?, calories = ?, carbohydrate = ?, fat = ?, protein = ? WHERE id = ?", false,
                    foodstuff.getProducer(), foodstuff.getCalories(), foodstuff.getCarbohydrate(), foodstuff.getFat(), foodstuff.getProtein(), foodstuff.getFoodstuffMetaId());

            db.update("UPDATE item SET location = ?, expiration = ? WHERE id = ?", false,
                    foodstuff.getLocation(), foodstuff.getExpiration(), foodstuff.getId());
        } else {
            throw new SecurityException("action is not authorized");
        }
    }

    public List<Foodstuff> mostPopular(User user, int limit) throws SQLException {
        String sql = "select count(g.id) as instances, g.id as globalreference_id from mealcomponent m, globalreference g, meal e, person p where p.identifier = e.person_identifier AND e.id = m.meal_id AND m.globalreference_id = g.id AND p.identifier = ? group by g.id order by instances desc limit ?;";

        List<Foodstuff> queryAndCollect = db.queryAndCollect("SELECT * FROM MealComponent WHERE meal_id = ?", rs -> {
            return findOne(rs.getInt("globalreference_id"));
        }, user.getId(), limit);
        return queryAndCollect;
    }

}
