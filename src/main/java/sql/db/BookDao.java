/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import storables.Book;
import storables.Item;

/**
 *
 * @author Janne
 */
public class BookDao {

    private Database db;

    public BookDao(Database db) {
        this.db = db;
    }

    public Book findOne(String uuid) throws SQLException {
        try (Connection connection = db.getConnection()) {
            List<Book> queryAndCollect = db.queryAndCollect("SELECT "
                    + "Item.uuid, "
                    + "Item.name, "
                    + "Item.serial_number, "
                    + "Item.created_on, "
                    + "Item.location AS location_id, "
                    + "Location.name AS location_name "
                    + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                    + "WHERE Item.deleted = 'false' AND Item.uuid = ?", rs -> {
                        return new Book(rs.getString("uuid"),
                                rs.getString("name"),
                                rs.getString("serial_number"),
                                rs.getString("location_name"),
                                rs.getInt("location_id"),
                                new Timestamp(rs.getString("created_on")),
                                MetaDao.getDescriptions(rs.getString("serial_number"), connection),
                                MetaDao.getTags(rs.getString("serial_number"), connection));
                    }, uuid);
            return queryAndCollect.get(0);
        }
    }

    public List<Book> findAll() throws SQLException {
        try (Connection connection = db.getConnection()) {
            List<Book> queryAndCollect = db.queryAndCollect("SELECT "
                    + "Item.uuid, "
                    + "Item.name, "
                    + "Item.serial_number, "
                    + "Item.created_on, "
                    + "Item.location AS location_id, "
                    + "Location.name AS location_name "
                    + "FROM Item LEFT JOIN Location ON Item.location = Location.id "
                    + "WHERE Item.deleted = 'false' AND Item.type = 'book'", rs -> {
                        return new Book(rs.getString("uuid"),
                                rs.getString("name"),
                                rs.getString("serial_number"),
                                rs.getString("location_name"),
                                rs.getInt("location_id"),
                                new Timestamp(rs.getString("created_on")),
                                MetaDao.getDescriptions(rs.getString("serial_number"), connection),
                                MetaDao.getTags(rs.getString("serial_number"), connection));
                    });
            return queryAndCollect;
        }
    }

    public Book create(Book t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void update(String key, Book t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
