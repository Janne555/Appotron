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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author janne
 */
public class MetaDao {

    public static Map<String, String> getTags(String serialNumber, String uuid, Connection connection) throws SQLException {
        Map<String, String> tags = new HashMap<>();
        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM Tag WHERE serial_number = ?");
        statement.setString(1, serialNumber);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            try {
                if (rs.getString("key").equals("expiration")) {
                    if (rs.getString("item_uuid").equals(uuid)) {
                        tags.put(rs.getString("key"), rs.getString("value"));
                        continue;
                    } else {
                        continue;
                    }
                }
                tags.put(rs.getString("key"), rs.getString("value"));
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        return tags;
    }

    public static List<String> getDescriptions(String serialNumber, Connection connection) throws SQLException {
        List<String> descriptions = new ArrayList<>();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM Description WHERE serial_number = ?");
            statement.setString(1, serialNumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                try {
                    descriptions.add(rs.getString("descriptor"));
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        
        return descriptions;
    }
}
