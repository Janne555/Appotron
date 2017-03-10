/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.WebMethods;
import java.sql.SQLException;
import java.util.UUID;
import org.mindrot.jbcrypt.BCrypt;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import sql.daos.ItemDao;
import sql.daos.ListItemDao;
import sql.daos.NutritionalInfoDao;
import sql.daos.ServingDao;
import sql.daos.ShoppingListDao;
import sql.daos.TagDao;
import sql.daos.UserDao;
import storables.User;
import util.PasswordUtil;

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
        Database database = null;
        if (true) {
            database = new Database("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/inventordev", "devaajat", "salasana");

            String[] strings = {"item", "users", "listitem", "loan", "nutritionalinfo", "serving", "shoppinglist", "tag"};
            for (String s : strings) {
                try {
                    database.update("DROP TABLE " + s + " CASCADE");
                } catch (SQLException e) {
                    if (!e.getMessage().contains("does not exist")) {
                        throw e;
                    }
                }
            }

            new DatabaseCreator("dbcommands.json", database);

            Testdata td = new Testdata("data.json");
            for (String sql : td.getInserts()) {
                System.out.println(sql);
                database.update(sql);
            }
        }

        ItemDao itemDao = new ItemDao(database);
        TagDao tagDao = new TagDao(database);
        ListItemDao liDao = new ListItemDao(database);
        ShoppingListDao slDao = new ShoppingListDao(database);
        UserDao uDao = new UserDao(database);
        ServingDao seDao = new ServingDao(database);
        NutritionalInfoDao nuDao = new NutritionalInfoDao(database);

        new WebMethods(itemDao, tagDao, liDao, slDao, uDao, seDao, nuDao);
    }
}
