/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import sql.db.Dao;
import sql.db.Database;
import storables.User;
import util.Incrementer;
import util.PasswordUtil;

public class UserDao implements Dao<User, String> {

    public UserDao() {
    }

    @Override
    public User store(User t, Database db) throws Exception {
        try (Connection con = db.getConnection()) {
            con.setAutoCommit(false);
            PreparedStatement ps1 = con.prepareStatement("CREATE user ? WITH PASSWORD ?");
            ps1.setObject(0, t.getUsername());
            ps1.setObject(1, t.getPassword());
            ps1.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement("INSERT INTO Users(name, email, password, dbpassword, apikey, date, deleted) VALUES(?, ?, ?, ?, ?, ?, ?)");
            Incrementer inc = new Incrementer();
            ps2.setObject(inc.next(), t.getUsername());
            ps2.setObject(inc.next(), t.getEmail());
            ps2.setObject(inc.next(), t.getPassword());
            ps2.setObject(inc.next(), t.getDbPassword());
            ps2.setObject(inc.next(), t.getApikey());
            ps2.setObject(inc.next(), new Timestamp(System.currentTimeMillis()));
            ps2.setObject(inc.next(), "false");

            con.commit();
        }

        return t;
    }

    @Override
    public List<User> search(Database db, String... searchWords) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User findOne(String key, Database db) throws Exception {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE id = ?", rs -> {
            return new User(rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("apikey"),
                    rs.getString("email"),
                    rs.getString("dbpassword"),
                    null);
        });

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    public User findOneByUserName(String key, Database db) throws Exception {
        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE username = ?", rs -> {
            return new User(rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("apikey"),
                    rs.getString("email"),
                    rs.getString("dbpassword"),
                    null);
        });

        if (queryAndCollect.isEmpty()) {
            return null;
        }

        return queryAndCollect.get(0);
    }

    @Override
    public List<User> findAll(Database db) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(User t, Database db) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String key, Database db) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    public User findUserByName(String username) throws SQLException {
//        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE name = ?", rs -> {
//            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
//        }, username);
//
//        if (queryAndCollect.isEmpty()) {
//            return null;
//        } else {
//            return queryAndCollect.get(0);
//        }
//    }
//
//    public User findUserById(String id) throws SQLException {
//        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE id = ?", rs -> {
//            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
//        }, id);
//
//        if (queryAndCollect.isEmpty()) {
//            return null;
//        } else {
//            return queryAndCollect.get(0);
//        }
//    }
//
//    public User findUserByApikey(String apikey) throws SQLException {
//        List<User> queryAndCollect = db.queryAndCollect("SELECT * FROM Users WHERE apikey = ?", rs -> {
//            return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"), rs.getString("apikey"));
//        }, apikey);
//
//        if (queryAndCollect.isEmpty()) {
//            return null;
//        } else {
//            return queryAndCollect.get(0);
//        }
//    }
//
//    public void createUser(User user) throws SQLException {
//        db.update("INSERT INTO Users(id, name, password, apikey, deleted) VALUES(?,?,?,?,?)",
//                user.getUuid(), user.getUsername(), PasswordUtil.hashPassword(user.getPassword()), user.getApikey(), false);
//    }
}
