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
                    sql += "Item(uuid, name, serial_number, location, created_on, deleted)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("serial_number").getAsString() + ", "
                            + o.get("location").getAsString() + ", "
                            + o.get("created_on").getAsString() + ", "
                            + "'false')";
                    break;
                case "book":
                    sql += "Book(uuid, title, author, publisher, publishing_year, isbn, location, created_on, deleted)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("title").getAsString() + ", "
                            + o.get("author").getAsString() + ", "
                            + o.get("publisher").getAsString() + ", "
                            + o.get("publishing_year").getAsString() + ", "
                            + o.get("isbn").getAsString() + ", "
                            + o.get("location").getAsString() + ", "
                            + o.get("created_on").getAsString() + ", "
                            + "'false')";
                    break;
                case "foodstuff":
                    sql += "Foodstuff(uuid, name, serial_number, location, expiration, created_on, deleted)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("serial_number").getAsString() + ", "
                            + o.get("location").getAsString() + ", "
                            + o.get("expiration").getAsString() + ", "
                            + o.get("created_on").getAsString() + ", "
                            + "'false')";
                    break;
                case "tag":
                    sql += "Tag(uuid, serial_or_isbn, key, value)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("serial_or_isbn").getAsString() + ", "
                            + o.get("key").getAsString() + ", "
                            + o.get("value").getAsString() + ")";
                    break;
                case "description":
                    sql += "Description(uuid, serial_or_isbn, descriptor)"
                            + " VALUES("
                            + o.get("uuid").getAsString() + ", "
                            + o.get("serial_or_isbn").getAsString() + ", "
                            + o.get("descriptor").getAsString() + ")";
                    break;
                default:
                    throw new AssertionError();
            }
            inserts.add(sql);
        }
    }

    public List<String> getInserts() {
        return inserts;
    }
    
    
}
