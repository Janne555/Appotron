/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.WebMethods;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.daos.ItemDao;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;

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

        if (System.getProperties().getProperty("cleardatabase").equals("true")) {
            String[] strings = {"item", "users", "listitem", "loan", "nutritionalinfo", "serving", "shoppinglist", "tag", "meal", "ingredient", "session"};
            for (String s : strings) {
                try {
                    database.update("DROP TABLE " + s + " CASCADE");
                } catch (SQLException e) {
                    if (!e.getMessage().contains("does not exist")) {
                        throw e;
                    }
                }
            }
        }
        
        if (System.getProperties().getProperty("createtables").equals("true")) {
            new DatabaseCreator("dbcommands.json", database);
        }
        
        if (System.getProperties().getProperty("insertdata").equals("true")) {
            Testdata td = new Testdata("data.json");
            for (String sql : td.getInserts()) {
                System.out.println(sql);
                database.update(sql);
            }
        }
        
        ItemDao itemDao = new ItemDao(database);
        List<Item> search = itemDao.search("poyta");
        for (Item it : search) {
            System.out.println(it);
        }
        new WebMethods(database);
    }
}
