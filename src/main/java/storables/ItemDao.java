/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sql.db.Database;
import sql.db.Search;

/**
 *
 * @author Janne
 */
public class ItemDao {

    private Database db;

    public ItemDao(Database db) {
        this.db = db;
    }

    public void insert(Item item) throws SQLException {
        String sql = "INSERT INTO Item "
                + "(uuid, name, serial_number, location, created_on, deleted)"
                + " VALUES ("
                + item.getUuid() + ", "
                + item.getName() + ", "
                + item.getSerial_number() + ", "
                + item.getLocationId() + ", "
                + item.getUuid() + ", ";
    }

    public List<Item> findAll() throws SQLException {
        String sql = "SELECT * FROM Item LEFT JOIN Location ON Item.location = Location.id";
        List<Item> items = db.queryAndCollect(sql, rs -> {
            return new Item(rs.getString("uuid"),
                    rs.getString("name"),
                    rs.getString("serial_number"),
                    rs.getString("location"),
                    rs.getInt("id"),
                    rs.getTimestamp("created_on"),
                    null,
                    null);
        });

        for (Item i : items) {
            sql = "SELECT * FROM Description WHERE serial_or_isbn = '" + i.getSerial_number() + "'";
            db.queryAndCollect(sql, rs -> {
//                rs.
                return null;
            });
        }

        return items;
    }

    public List<Item> find(String searchTerm, Search search) {
        List<Item> items = new ArrayList<>();
        switch (search) {
            case NAME:

                break;
            case TAG:

                break;

            case SERIAL:

                break;
            case UUID:

                break;
            case CREATED_ON:

                break;
            default:
                throw new AssertionError();
        }
        return null;
    }
}
