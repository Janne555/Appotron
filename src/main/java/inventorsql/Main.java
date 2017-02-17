/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Table;
import sql.db.Testdata;
import storables.Item;
import storables.ItemDao;

/**
 *
 * @author Janne
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws Exception {
        try {
            File f = new File("inventor.db");
            f.delete();
        } catch (Exception ex) {
        }

        Database database = new Database("org.sqlite.JDBC", "jdbc:sqlite:inventor.db");

        DatabaseCreator creator = new DatabaseCreator("dbcommands.json", database);

        Testdata td = new Testdata("data.json");

        for (String sql : td.getInserts()) {
            System.out.println(sql);

            database.update(sql);
        }

        for (Item i : new ItemDao(database).findAll()) {
            System.out.println(i);
        }

//        for (int i = 0; i < 10; i++) {
//            System.out.println(uuid());
//        }
//        
//        database.update(sql);
//        
//        System.out.println(database.getLocationKey("kaappi"));
//        System.out.println(database.getLocationValue(1));
//        System.out.println(new ItemDao(database).findAll());
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String time() {
        return new Timestamp(System.currentTimeMillis()).toString();
    }

    public static String serialize(String string) {
        String result = "";
        for (char c : string.toCharArray()) {
            switch (c) {
                case 's':
                    c = '5';
                    break;
                case 'i':
                    c = '1';
                    break;
                case 't':
                    c = '7';
                    break;
                case 'a':
                    c = '4';
                    break;
                case 'e':
                    c = '3';
                    break;
                case 'b':
                    c = '8';
                    break;
                case '0':
                    c = '0';
                    break;
                default:
                    throw new AssertionError();
            }

            result += c;
        }
        return result;
    }
}
