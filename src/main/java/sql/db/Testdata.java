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
import java.sql.Timestamp;
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
            switch (o.get("table").getAsString()) {
                case "ItemInfo":
                    sql += "ItemInfo(name, type, identifier)"
                            + " VALUES("
                            + o.get("name").getAsString() + ", "
                            + o.get("type").getAsString() + ", "
                            + o.get("identifier").getAsString() + ")";
                    break;
                case "Item":
                    sql += "Item(id, iteminfo_identifier, location, owner, date, expiration, deleted)"
                            + " VALUES("
                            + o.get("id").getAsString() + ", "
                            + o.get("iteminfo_identifier").getAsString() + ", "
                            + o.get("owner").getAsString() + ", "
                            + new Timestamp(System.currentTimeMillis()).toString() + ", "
                            + new Timestamp(System.currentTimeMillis() + 1000000).toString() + ", "
                            + "'false'" + ")";
                    break;
                case "ItemInfoTag":
                    sql += "ItemInfoTag(identifier, key, value)"
                            + " VALUES("
                            + o.get("identifier").getAsString() + ", "
                            + o.get("key").getAsString() + ", "
                            + o.get("value").getAsString() + ")";
                    break;
                case "ItemSpecificTag":
                    sql += "ItemSpecificTag(identifier, key, value)"
                            + " VALUES("
                            + o.get("identifier").getAsString() + ", "
                            + o.get("key").getAsString() + ", "
                            + o.get("value").getAsString() + ")";
                    break;
                case "ShoppingList":
                    sql += "ShoppingList(id, name, date, deleted)"
                            + " VALUES("
                            + o.get("id").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("date").getAsString() + ", "
                            + "'false'" + ")";
                    break;
                case "ListItem":
                    sql += "ListItem(iteminfo_identifier, shoppinglist_id, amount)"
                            + " VALUES("
                            + o.get("iteminfo_identifier").getAsString() + ", "
                            + o.get("shoppinglist_id").getAsString() + ", "
                            + o.get("amount").getAsString() + ")";
                    break;
                case "Users":
                    sql += "Users(id, name, email, password, apikey, deleted)"
                            + " VALUES("
                            + o.get("id").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("email").getAsString() + ", "
                            + o.get("password").getAsString() + ", "
                            + o.get("apikey").getAsString() + ", "
                            + "'false'" + ")";
                    break;
                case "Serving":
                    sql += "Serving(meal_identifier, users_id, mass, date, deleted)"
                            + " VALUES("
                            + o.get("user_uuid").getAsString() + ", "
                            + o.get("identifier").getAsString() + ", "
                            + o.get("mass").getAsString() + ", "
                            + "'" + new Timestamp(System.currentTimeMillis()).toString() + "', "
                            + "'false'" + ")";
                    break;
                case "meal":
                    sql += "Meal(id, name, type, deleted)"
                            + " VALUES("
                            + o.get("id").getAsString() + ", "
                            + o.get("name").getAsString() + ", "
                            + o.get("main").getAsString() + ", "
                            + "'false'" + ")";
                    break;
                case "ingredient":
                    sql += "Ingredient(meal_id, item_identifier, percentage, deleted)"
                            + " VALUES("
                            + o.get("meal_id").getAsString() + ", "
                            + o.get("item_identifier").getAsString() + ", "
                            + o.get("percentage").getAsString() + ", "
                            + "'false'" + ")";
                    break;
                case "nutritionalinfo":
                    sql += "NutritionalInfo(identifier, energy, carbohydrate, fat, protein)"
                            + " VALUES("
                            + o.get("identifier").getAsString() + ", "
                            + o.get("energy").getAsString() + ", "
                            + o.get("carbohydrate").getAsString() + ", "
                            + o.get("fat").getAsString() + ", "
                            + o.get("protein").getAsString() + ")";
                    break;
                case "description":
                    continue;
            }

//            if (sql.equals("INSERT INTO ")) continue;
            inserts.add(sql);
        }
    }

    public List<String> getInserts() {
        return inserts;
    }

}
