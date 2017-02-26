/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import storables.Item;
import storables.Tag;

/**
 *
 * @author Janne
 */
public class TagDao {

    private String select;

    private Database db;

    public TagDao(Database db) {
        this.db = db;
        this.select = "SELECT * FROM Tag";
    }
    
    public Tag findOne(int id) throws SQLException {
        List<Tag> queryAndCollect = db.queryAndCollect(select + " WHERE id = ?", rs -> {
            return new Tag(rs.getInt("id"),
                    rs.getString("identifier"),
                    rs.getString("key"),
                    rs.getString("value"),
                    rs.getString("type"));
        }, id);
        return queryAndCollect.get(0);
    }
    
    public List<Tag> findAll() throws SQLException {
        List<Tag> queryAndCollect = db.queryAndCollect(select, rs -> {
            return new Tag(rs.getInt("id"),
                    rs.getString("identifier"),
                    rs.getString("key"),
                    rs.getString("value"),
                    rs.getString("type"));
        });
        return queryAndCollect;
    }

    public List<Tag> findAllByIdentifier(String identifier) throws SQLException {
        List<Tag> queryAndCollect = db.queryAndCollect(select + " WHERE identifier = ?", rs -> {
            return new Tag(rs.getInt("id"),
                    rs.getString("identifier"),
                    rs.getString("key"),
                    rs.getString("value"),
                    rs.getString("type"));
        }, identifier);

        return queryAndCollect;
    }

    public void create(Tag t) throws SQLException {
        db.update("INSERT INTO Tag(identifier, key, value, type) VALUES(?, ?, ?, ?)", t.getObjs());
    }

    public void create(List<Tag> tags) throws SQLException {
        for (Tag t : tags) {
            create(t);
        }
    }

    public void update(Tag t) throws SQLException {
        db.update("UPDATE Tag SET item_uuid = ?, serial_number = ?, key = ?, value = ? WHERE id = ?", t.getObjsId());
    }

    public void delete(String key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
