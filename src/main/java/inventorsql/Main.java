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
import sql.db.BookDao;
import sql.db.FoodstuffDao;
import sql.db.ItemDao;

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
        
        for (Item i : new BookDao(database).findAll()) {
            System.out.println(i);
        }
        
        System.out.println(new BookDao(database).findOne("9266c739-b79d-4da4-b339-7c23eea3c00e"));

        get("/everything", (req, res) -> {
            HashMap map = new HashMap<>();
            ItemDao itemDao = new ItemDao(database);
            if (req.queryParams("uuid") != null) {
                List<Item> list = new ArrayList<>();
                list.add(itemDao.findOne(req.queryParams("uuid")));
                map.put("items", list);
            } else {
                map.put("items", itemDao.findAll());
            }

            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

    }
}
