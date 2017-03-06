/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.ItemHelper;
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
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.db.ItemDao;
import static sql.db.JsonUtil.json;
import sql.db.ListItemDao;
import sql.db.Param;
import sql.db.ShoppingListDao;
import sql.db.TagDao;
import sql.db.Type;
import storables.ListItem;
import storables.ShoppingList;
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
//        if (localhost) {
//            String projectDir = System.getProperty("user.dir");
//            String staticDir = "/src/main/resources/public";
//            staticFiles.externalLocation(projectDir + staticDir);
//        } else {
//            staticFiles.location("/public");
//        }

        try {
            File f = new File("inventor.db");
            f.delete();
        } catch (Exception ex) {
        }

        Database database = new Database("org.sqlite.JDBC", "jdbc:sqlite:inventor.db");
        DatabaseCreator creator = new DatabaseCreator("dbcommands.json", database);
        Testdata td = new Testdata("data.json");

        for (String sql : td.getInserts()) {
            database.update(sql);
        }

        ItemDao itemDao = new ItemDao(database);
        TagDao tagDao = new TagDao(database);
        ListItemDao liDao = new ListItemDao(database);
        ShoppingListDao slDao = new ShoppingListDao(database);

        for (ListItem li : liDao.findAll()) {
            System.out.println(li);
        }

        get("/expiring.get", (reg, res) -> {
            System.out.println("received request for expiring foodstuffs");
            return itemDao.getExpiring(5);
        }, json());

        get("/testing", (req, res) -> new ModelAndView(new HashMap<>(), "testing"), new ThymeleafTemplateEngine());

        get("/tags.get", (req, res) -> {
            System.out.println("received request for tags: " + req.queryParams("param"));

            return tagDao.findAllByIdentifier(req.queryParams("param"));
        }, json());

        post("/additem.post", (req, res) -> {
            for (String s : req.queryParams()) {
                System.out.println(s);
            }
            String name = req.queryParams("name");
            Type type = Type.parseType(req.queryParams("type"));
            String serialNumber = req.queryParams("serialNumber");
            String uuid = UUID.randomUUID().toString();
            String location = req.queryParams("location");
            if (type == Type.FOODSTUFF) {
                System.out.println(req.queryParams("expiration"));
            }

            List<Tag> tags = ItemHelper.parseTags(req, uuid);

            Item item = new Item(uuid, name, serialNumber, location, new Timestamp(System.currentTimeMillis()), tags, type);

            System.out.println(item);

            try {
                itemDao.create(item);
            } catch (SQLException ex) {
                res.redirect("/fail?msg=" + ex.toString());
            }

            res.redirect("/");
            return "ok";
        });

        get("/additem", (req, res) -> {
            HashMap map = new HashMap();
            map.put("locations", itemDao.getLocations());
            String name = "";
            String serialNumber = "";
            if (!req.queryParams().isEmpty()) {
                name += req.queryParams("name");
                serialNumber += req.queryParams("serial");
            }
            map.put("namefield", name);
            map.put("serialfield", serialNumber);
            return new ModelAndView(map, "additem");
        }, new ThymeleafTemplateEngine());

        get("/fail", (req, res) -> {
            HashMap map = new HashMap();
            map.put("msg", req.queryParams("msg"));
            return new ModelAndView(map, "fail");
        }, new ThymeleafTemplateEngine());

        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("items", itemDao.getExpiring(5));
            ShoppingList sl = slDao.findOne("0");
            map.put("listname", "Shopping List: " + sl.getName());
            map.put("listitems", sl.getListItems());
            return new ModelAndView(map, "index");
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
