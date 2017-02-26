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
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.db.ItemDao;
import sql.db.Param;
import sql.db.TagDao;
import storables.Tag;

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
//            System.out.println(sql);

            database.update(sql);
        }

        ItemDao itemDao = new ItemDao(database);
        TagDao tagDao = new TagDao(database);

        tagDao.create(new Tag(0, "d982c17e-ccdb-4b9f-bb6b-130c989e4b16", "m4170", "käyttöaste", "kulunut"));
        
        Tag findOne = tagDao.findOne(31);
        findOne.setValue("uuden veroinen");
        System.out.println(findOne);
        tagDao.update(findOne);

//        for (Tag tag : tagDao.findAll()) {
//            System.out.println(tag);
//        }
//
//        for (Item it : itemDao.findAll()) {
//            System.out.println(it);
//        }

        System.out.println(itemDao.findOne("d982c17e-ccdb-4b9f-bb6b-130c989e4b16"));

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
                    searchTerms.put(Param.parseParam(queryParam), req.queryParams(queryParam));
                }
                map.put("items", itemDao.findBy(searchTerms));

            } else {
            }
            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/tags", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("tags", tagDao.findAll());
            return new ModelAndView(map, "dropdown");
        }, new ThymeleafTemplateEngine());

    }

}
