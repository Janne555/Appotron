/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.WebMethods;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import sql.daos.ItemDao;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;
import storables.User;
import util.Type;

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
        InputStream input;

        List<String> makeTables = new ArrayList<>();
        File f = new File("createtables.sql");
        Scanner r = new Scanner(f).useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));
        while (r.hasNext()) {
            makeTables.add(r.next());
        }

        try {
            input = new FileInputStream("application.configuration");

            System.getProperties().load(input);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Database database = new Database("org.postgresql.Driver",
                System.getProperties().getProperty("postgre_address"),
                System.getProperties().getProperty("postgre_user"),
                System.getProperties().getProperty("postgre_password"));

        if (System.getProperties().getProperty("standalone").equals("true")) {
            String[] strings = {"iteminfo",
                "item",
                "nutritionalinfo",
                "iteminfotag",
                "listitem",
                "shoppinglist",
                "itemspecifictag",
                "loan",
                "users",
                "sessioncontrol",
                "bugreport",
                "meal",
                "accesscontrol",
                "mealcomponent"};

            for (String s : strings) {
                try {
                    database.update("drop table " + s + " cascade");
                } catch (Exception e) {

                }
            }
        }

        if (System.getProperties().getProperty("standalone").equals("true")) {
            for (String s : makeTables) {
                System.out.println(s);
                database.update(s);
            }
        }

        database.update("INSERT INTO Users(id, name, password, date, deleted) VALUES(?,?,?,?,?)", "jannetar", "janne", "salis", new Timestamp(System.currentTimeMillis()), false);
        database.update("INSERT INTO Users(id, name, password, date, deleted) VALUES(?,?,?,?,?)", "salakaveri", "kaveri", "salis", new Timestamp(System.currentTimeMillis()), false);

        database.update("INSERT INTO ItemInfo(type, name, identifier) VALUES(?,?,?)", "item", "tavara", "tavarainen");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 1, "muoto", "littana");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 1, "arvo", "turha");
        database.update("INSERT INTO Item(iteminfo_id, location, date, deleted) VALUES(?,?,?,?)", 1, "kaappi", new Timestamp(System.currentTimeMillis()), false);
        database.update("INSERT INTO ItemSpecificTag(item_id, key, value) VALUES(?,?,?)", 1, "arvio", "mahtava");
        database.update("INSERT INTO AccessControl VALUES(?,?,?)", 1, "jannetar", 1);

        database.update("INSERT INTO ItemInfo(type, name, identifier) VALUES(?,?,?)", "item", "salainen tavara", "salatar");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 2, "muoto", "kulmikas");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 2, "arvo", "mittaamattoman kallis");
        database.update("INSERT INTO Item(iteminfo_id, location, date, deleted) VALUES(?,?,?,?)", 2, "kaappi", new Timestamp(System.currentTimeMillis()), false);
        database.update("INSERT INTO ItemSpecificTag(item_id, key, value) VALUES(?,?,?)", 2, "arvosana", "huono");
        database.update("INSERT INTO AccessControl VALUES(?,?,?)", 2, "salakaveri", 1);

        database.update("INSERT INTO ItemInfo(type, name, identifier) VALUES(?,?,?)", "item", "duplikaatti", "duplikoiva");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 3, "muoto", "taiteellinen");
        database.update("INSERT INTO ItemInfoTag(iteminfo_id, key, value) VALUES(?,?,?)", 3, "arvo", "semikallis");
        database.update("INSERT INTO Item(iteminfo_id, location, date, deleted) VALUES(?,?,?,?)", 3, "kaappi", new Timestamp(System.currentTimeMillis()), false);
        database.update("INSERT INTO ItemSpecificTag(item_id, key, value) VALUES(?,?,?)", 3, "kunto", "rikki");
        database.update("INSERT INTO AccessControl VALUES(?,?,?)", 3, "salakaveri", 1);

        database.update("INSERT INTO Item(iteminfo_id, location, date, deleted) VALUES(?,?,?,?)", 3, "kaappi", new Timestamp(System.currentTimeMillis()), false);
        database.update("INSERT INTO ItemSpecificTag(item_id, key, value) VALUES(?,?,?)", 4, "kunto", "ehyt");
        database.update("INSERT INTO AccessControl VALUES(?,?,?)", 4, "jannetar", 1);

//        User user = new User("jannetar", "janne", "salis", null, null, null, null);
//        ItemDao itemDao = new ItemDao(database);
//        
//        for (Item it : itemDao.search(user, "kaappi", "littana")) {
//            System.out.println(it);
//        }
        
//        List<Item> search = itemDao.search(user, "poyta");
//        for (Item it : search) {
//            System.out.println(it);
//        }
//        new WebMethods(database);
    }
}
