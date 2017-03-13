/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.User;
import util.PasswordUtil;

public class UserDao {

    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }

    public User findUserByName(String username) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE name = ?", rs -> {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
        }, username);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    public User findUserById(String id) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE id = ?", rs -> {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
        }, id);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    public User findUserByApikey(String apikey) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE apikey = ?", rs -> {
            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
        }, apikey);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }

    public void createUser(User user) throws SQLException {
        db.update("INSERT INTO Users(id, name, password, apikey, deleted) VALUES(?,?,?,?,?)",
                user.getUuid(), user.getUsername(), PasswordUtil.hashPassword(user.getPassword()), user.getApikey(), false);
    }
}
