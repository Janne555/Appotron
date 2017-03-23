/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.Tag;

/**
 *
 * @author Janne
 */
public class ItemSpecificTagDao {

    private Database db;

    public ItemSpecificTagDao(Database db) {
        this.db = db;
    }

    public Tag store(Tag t) throws SQLException {
        db.update("INSERT INTO ItemSpecificTag(item_id, key, value) VALUES(?,?,?)", false, t.getItemId(), t.getKey(), t.getValue());
        return t;
    }

    public Tag[] store(Tag... tags) throws Exception {
        for (Tag t : tags) {
            store(t);
        }

        return tags;
    }

    public List<Tag> store(List<Tag> tags) throws Exception {
        if (tags == null) return null;
        for (Tag t : tags) {
            store(t);
        }

        return tags;
    }

    public List<Tag> findAll(int itemId) throws SQLException {
        List<Tag> queryAndCollect = db.queryAndCollect("SELECT DISTINCT ON (id) * FROM ItemSpecificTag WHERE item_id = ?", rs -> {
            return new Tag(rs.getInt("id"),
                    rs.getInt("item_id"),
                    rs.getString("key"),
                    rs.getString("value"));
        }, itemId);
        return queryAndCollect;
    }

//    public Tag findOne(int id) throws SQLException {
//        List<Tag> queryAndCollect = db.queryAndCollect(select + " WHERE id = ?", rs -> {
//            return new Tag(rs.getInt("id"),
//                    rs.getString("identifier"),
//                    rs.getString("key"),
//                    rs.getString("value"),
//                    rs.getString("type"));
//        }, id);
//        return queryAndCollect.get(0);
//    }
//    
//    public List<Tag> findAll() throws SQLException {
//        List<Tag> queryAndCollect = db.queryAndCollect(select, rs -> {
//            return new Tag(rs.getInt("id"),
//                    rs.getString("identifier"),
//                    rs.getString("key"),
//                    rs.getString("value"),
//                    rs.getString("type"));
//        });
//        return queryAndCollect;
//    }
//
//
//    public void create(Tag t) throws SQLException {
//        db.update("INSERT INTO Tag(identifier, key, value, type) VALUES(?, ?, ?, ?)", t.getObjs());
//    }
//
//    public void create(List<Tag> tags) throws SQLException {
//        for (Tag t : tags) {
//            create(t);
//        }
//    }
//
//    public void update(Tag t) throws SQLException {
//        db.update("UPDATE Tag SET identifier = ?, key = ?, value = ?, type = ? WHERE id = ?", t.getObjsId());
//    }
//
//    public void delete(int id) throws SQLException {
//        db.update("DELETE FROM Tag WHERE id = ?", id);
//    }
}
