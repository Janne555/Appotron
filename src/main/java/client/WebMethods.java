/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import storables.Item;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
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
import storables.ShoppingList;
import storables.Tag;
import storables.User;
import util.Service;

/**
 *
 * @author Janne
 */
public class WebMethods {

    ItemDao itemDao;
    TagDao tagDao;
    ListItemDao liDao;
    ShoppingListDao slDao;
    UserDao uDao;

    public WebMethods(ItemDao itemDao, TagDao tagDao, ListItemDao liDao, ShoppingListDao slDao, UserDao uDao) {
        this.itemDao = itemDao;
        this.tagDao = tagDao;
        this.liDao = liDao;
        this.slDao = slDao;
        this.uDao = uDao;

        setupRoutes();
    }

    private void setupRoutes() {
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("user", req.session().attribute("user"));

            map.put("items", itemDao.getExpiring(5));
            ShoppingList sl = slDao.findOne("0");
            map.put("listname", "Shopping List: " + sl.getName());
            map.put("listitems", sl.getListItems());
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/logout", (req, res) -> {
            HashMap map = new HashMap();
            req.session().removeAttribute("user");
            res.redirect("/");
            return new ModelAndView(map, "login");
        });
        before("/logout", (req, res) -> {
            if (req.session().attribute("user") == null) {
                halt("No user logged in");
            }
        });

        get("/expiring.get", (req, res) -> {
            System.out.println("received request for expiring foodstuffs");
            return itemDao.getExpiring(5);
        }, json());

        get("/testing", (req, res) -> new ModelAndView(new HashMap<>(), "testing"), new ThymeleafTemplateEngine());

        get("/tags.get", (req, res) -> {
            System.out.println("received request for tags: " + req.queryParams("param"));

            return tagDao.findAllByIdentifier(req.queryParams("param"));
        }, json());

        get("/additem", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));
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
            map.put("user", req.session().attribute("user"));
            map.put("msg", req.queryParams("msg"));
            return new ModelAndView(map, "fail");
        }, new ThymeleafTemplateEngine());

        get("/find", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("user", req.session().attribute("user"));
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
            map.put("user", req.session().attribute("user"));
            map.put("tags", tagDao.findAll());
            return new ModelAndView(map, "dropdown");
        }, new ThymeleafTemplateEngine());

        get("/login", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        get("/profile/:username", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));

            User user = (User) req.session().attribute("user");

            checkLogin(req, res);

            return new ModelAndView(map, "profile");
        }, new ThymeleafTemplateEngine());

        get("/addserving", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));
            
            checkLogin(req, res);
            
            return new ModelAndView(map, "addserving");
        }, new ThymeleafTemplateEngine());

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            LoginResult checkUser = Service.checkUser(new User(null, username, password), uDao);

            if (checkUser.getUser() == null) {
                res.redirect("fail?msg=" + checkUser.getError());
                halt();
            }

            req.session(true).attribute("user", checkUser.getUser());
            res.redirect("/");
            return "";
        });
        before("/login", (req, res) -> {
            if (req.session().attribute("user") != null) {
                halt("Already logged in");
            }
        });

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

    }

    private void checkLogin(Request req, Response res) {
        User user = (User) req.session().attribute("user");

        if (user == null) {
            res.redirect("/login");
            halt();
        } else if (req.params(":username") != null && !user.getUsername().equals(req.params(":username"))) {
            res.redirect("/fail?msg=access_denied");
            halt();
        }
    }
}
