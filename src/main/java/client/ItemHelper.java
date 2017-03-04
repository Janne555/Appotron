/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;
import spark.Request;
import sql.db.Type;
import storables.Tag;

/**
 *
 * @author Janne
 */
public class ItemHelper {

    public ItemHelper() {
    }

    public static List<Tag> parseTags(Request req, String uuid) {
        List<Tag> tags = new ArrayList<>();
        for (String s : req.queryParams()) {
            if (s.contains("tag:")) {
                String key = s.substring(4);
                String value = req.queryParams(s);
                tags.add(new Tag(0, req.queryParams("serialNumber"), key, value, Type.SERIAL));
            }
        }
        String expiration = req.queryParams("expiration");

        if (!expiration.isEmpty()) {
            expiration += " 00:00:00";
            tags.add(new Tag(0, uuid, "expiration", expiration, Type.UUID));
        }
        
        return tags;
    }
}
