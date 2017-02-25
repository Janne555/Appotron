/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;
import sql.db.ItemDao;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.db.ItemDao;
import storables.Foodstuff;

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

        ItemDao itemDao = new ItemDao(database);
        
        for (Item it : itemDao.findAll()) {
            System.out.println(it);
        }
        
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            List<Item> list = new ArrayList<>();
            list.addAll(itemDao.findAll());
            map.put("items", list);
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/find", (req, res) -> {
            HashMap map = new HashMap<>();
            String type;
            if ((type = req.queryParams("type")) != null && req.queryParams("all").equals("true")) {
                switch (type) {
                    case "item":
                        map.put("items", itemDao.findAll());
                        break;
                    case "foodstuff":
                        break;
                    case "book":
                        break;
                    default:
                        throw new AssertionError();
                }
            } else {
            }
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());
    }
}
