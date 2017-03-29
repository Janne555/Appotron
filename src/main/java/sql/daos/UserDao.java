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
import storables.User;

public class UserDao {

    private Database db;

    public UserDao(Database db) {
        this.db = db;
    }

    public User store(User t) throws Exception {
        db.update("INSERT INTO person(identifier, name, email, password, apikey, date, deleted) VALUES(?,?,?,?,?,?,?)", false,
                t.getId(),
                t.getUsername(),
                t.getEmail(),
                t.getPassword(),
                t.getApikey(),
                new Timestamp(System.currentTimeMillis()),
                false);
        return t;
    }

    public User findById(String id) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM person WHERE identifier = ?", rs -> {
            return new User(rs.getString("identifier"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("apikey"),
                    rs.getString("email"));
        }, id);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public User findByName(String name) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM person WHERE name = ?", rs -> {
            return new User(rs.getString("identifier"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("apikey"),
                    rs.getString("email"));
        }, name);

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public User findByApiKey(String apikey) throws SQLException {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM person WHERE apikey = ?", rs -> {
            return new User(rs.getString("identifier"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("apikey"),
                    rs.getString("email"));
        }, apikey);

        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }
}
