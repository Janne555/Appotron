/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Janne
 */
public class Testdata {
    private List<String> inserts;

    public Testdata(String filename) throws FileNotFoundException {
        File f = new File(filename);
        Reader r = new FileReader(f);
        JsonObject json = new JsonParser().parse(r).getAsJsonObject();
        JsonArray asJsonArray = json.get("data").getAsJsonArray();
        this.inserts = new ArrayList<>();
        init(asJsonArray);
    }

    private void init(JsonArray asJsonArray) {
        for (JsonElement element : asJsonArray) {
            JsonObject o = element.getAsJsonObject();
            String sql = "INSERT INTO ";
            switch (o.get("type").getAsString()) {
                case "location":
                    sql += "Location(name, deleted) VALUES("
                            + o.get("name").getAsString() + ", 'false')";
                    break;
                case "item":
                    sql += "Item(uuid, name, serial_number, location, created_on, type, deleted)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("serial_number").getAsString() + ", "
                            + o.get("location").getAsString() + ", "
                            + o.get("created_on").getAsString() + ", "
                            + o.get("main").getAsString() + ", "
                            + "'false')";
                    break;
                case "tag":
                    sql += "Tag(uuid, serial_number, key, value)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("serial_number").getAsString() + ", "
                            + o.get("key").getAsString() + ", "
                            + o.get("value").getAsString() + ")";
                    break;
                case "description":
                    sql += "Description(uuid, serial_number, descriptor)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("serial_number").getAsString() + ", "
                            + o.get("descriptor").getAsString() + ")";
                    break;
                case "expiration":
                    sql += "ExpirationDate(item_uuid, expiration)"
                            + " VALUES("
                            + o.get("item_uuid").getAsString() + ", "
                            + o.get("expiration").getAsString() + ")";
            }
            inserts.add(sql);
        }
    }

    public List<String> getInserts() {
        return inserts;
    }
    
    
}
