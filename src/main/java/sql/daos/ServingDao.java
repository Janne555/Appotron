/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Serving;
import storables.User;

public class ServingDao {

    private Database db;
    private ItemDao itemDao;
    private NutritionalInfoDao nuDao;

    public ServingDao(Database db) {
        this.db = db;
        this.itemDao = new ItemDao(db);
        this.nuDao = new NutritionalInfoDao(db);
    }

    public List<Serving> findByUser(User user) throws SQLException {
        List<Serving> queryAndCollect = db.queryAndCollect("SELECT * "
                + "FROM Serving LEFT JOIN Item ON Serving.identifier = Item.serial_number "
                + "WHERE Serving.deleted = 'false' AND Serving.user_uuid = ?", rs -> {
                    return new Serving(rs.getInt("Serving.id"),
                            user.getUuid(),
                            user,
                            rs.getString("Serving.identifier"),
                            itemDao.findOne(rs.getString("Serving.identifier")),
                            nuDao.findOne(rs.getString("Serving.identifier")),
                            rs.getFloat("Serving.mass"),
                            rs.getTimestamp("date"));
                }, user.getUuid());

        return queryAndCollect;
    }

    public void createServing(Serving serving) throws SQLException {
        db.update("INSERT INTO Serving(user_uuid, identifier, mass, date, deleted) VALUES(?, ?, ?, ?, ?)",
                serving.getUserUuid(), serving.getIdentifier(), serving.getMass(), serving.getDate(), false);
    }

    public List<Serving> findTodaysByUser(User user) throws SQLException {
        List<Serving> queryAndCollect = db.queryAndCollect("SELECT * "
                + "FROM Serving "
                + "WHERE deleted = 'false' AND user_uuid = ? AND date::date = CURRENT_DATE", rs -> {
                    return new Serving(rs.getInt("id"),
                            user.getUuid(),
                            user,
                            rs.getString("identifier"),
                            itemDao.findOneBySerial(rs.getString("identifier")),
                            nuDao.findOne(rs.getString("identifier")),
                            rs.getFloat("mass"),
                            rs.getTimestamp("date"));
                }, user.getUuid());

        return queryAndCollect;
    }
}
