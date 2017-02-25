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
import javafx.util.Pair;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;
import sql.db.ItemDao;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.db.ItemDao;
import sql.db.Search;
import sql.db.Type;

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
        
        for (Item it : itemDao.findBy(new HashMap<>())) {
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
            if (req.queryParams("all").equals("true")) {
                HashMap searchTerms = new HashMap<>();
                for (String queryParam : req.queryParams()) {
                    if (queryParam.equals("all")) {
                        continue;
                    }
                    searchTerms.put(Search.parseSearch(queryParam), req.queryParams(queryParam));
                }
                map.put("items", itemDao.findBy(searchTerms));
                
            } else {
            }
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());
    }
    
    public static Pair<Search, String>[] getPairs(List<Pair<Search, String>> stuff) {
        Pair<Search, String>[] pairs = new Pair[stuff.size()];
        for (int i = 0; i < stuff.size(); i++) {
            pairs[i] = stuff.get(i);
        }
        return pairs;
    }
}
