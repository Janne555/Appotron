/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import storables.Item;

/**
 *
 * @author Janne
 */
public class ItemCollector implements Collector<Item> {

    @Override
    public Item collect(ResultSet rs) throws SQLException {
        return new Item(rs.getString("uuid"), rs.getString("name"), rs.getString("serial_number"), rs.getString("location"), rs.getInt("location_id"), new Timestamp(rs.getString("created_on")), null, null);
    }
    
}
