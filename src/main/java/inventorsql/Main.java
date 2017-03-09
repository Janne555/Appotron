/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.ItemHelper;
import client.WebMethods;
import com.google.gson.JsonObject;
import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;
import spark.ModelAndView;
import spark.ResponseTransformer;
import spark.Session;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.daos.ItemDao;
import static util.JsonUtil.json;
import sql.daos.ListItemDao;
import util.Param;
import sql.daos.ShoppingListDao;
import sql.daos.TagDao;
import sql.daos.UserDao;
import sql.db.LoginResult;
import util.Type;
import storables.ListItem;
import storables.ShoppingList;
import storables.Tag;
import storables.User;
import util.PasswordUtil;
import util.Service;

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
            try {
                File f = new File("inventor.db");
                f.delete();
            } catch (Exception ex) {
            }

            database = new Database("org.sqlite.JDBC", "jdbc:sqlite:inventor.db");
            DatabaseCreator creator = new DatabaseCreator("dbcommands.json", database);
            Testdata td = new Testdata("data.json");

            for (String sql : td.getInserts()) {
                database.update(sql);
            }

        } else {
            database = new Database("org.sqlite.JDBC", "jdbc:sqlite:inventor.db");
        }

        ItemDao itemDao = new ItemDao(database);
        TagDao tagDao = new TagDao(database);
        ListItemDao liDao = new ListItemDao(database);
        ShoppingListDao slDao = new ShoppingListDao(database);
        UserDao uDao = new UserDao(database);
        
        uDao.createUser(new User(UUID.randomUUID().toString(), "janne", "salis"));

        new WebMethods(itemDao, tagDao, liDao, slDao, uDao);

    }
}
