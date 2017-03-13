/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
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
import sql.daos.NutritionalInfoDao;
import sql.daos.ServingDao;
import util.Param;
import sql.daos.ShoppingListDao;
import sql.daos.TagDao;
import sql.daos.UserDao;
import sql.db.LoginResult;
import storables.NutritionalInfo;
import storables.Serving;
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
    ServingDao seDao;
    NutritionalInfoDao nuDao;

    public WebMethods(ItemDao itemDao, TagDao tagDao, ListItemDao liDao, ShoppingListDao slDao, UserDao uDao, ServingDao seDao, NutritionalInfoDao nuDao) {
        this.itemDao = itemDao;
        this.tagDao = tagDao;
        this.liDao = liDao;
        this.slDao = slDao;
        this.uDao = uDao;
        this.seDao = seDao;
        this.nuDao = nuDao;

        setupRoutes();
    }

    private void setupRoutes() {
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("user", req.session().attribute("user"));

            map.put("items", itemDao.getExpiring(5));
            ShoppingList sl = slDao.findOne(0);

            if (sl != null) {
                map.put("listitems", sl.getListItems());
            }

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        get("/addnutritionalinfo", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("user", req.session().attribute("user"));
            String serial = req.queryParams("serial");

            //in case of return link from barcode reader app
            if (serial != null) {
                map.put("serialfield", serial);
            } else {
                map.put("serialfield", "");
            }

            return new ModelAndView(map, "addnutritionalinfo");
        }, new ThymeleafTemplateEngine());

        get("/mealdiary", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("user", req.session().attribute("user"));
            checkLogin(req, res, "/mealdiary");
            map.put("servings", seDao.findTodaysByUser((User) req.session().attribute("user")));
            return new ModelAndView(map, "mealdiary");
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
            List<Tag> list = tagDao.findAllByIdentifier(req.queryParams("param"));
            for (Tag t : list) {
                System.out.println(t);
            }
            return list;
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
            map.put("redirect", req.queryParams("redirect"));
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        get("/profile/:username", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));

            User user = (User) req.session().attribute("user");

            checkLogin(req, res, "/profile/" + user.getUsername());

            return new ModelAndView(map, "profile");
        }, new ThymeleafTemplateEngine());

        get("/addserving", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));

            checkLogin(req, res, "/addserving");

            return new ModelAndView(map, "addserving");
        }, new ThymeleafTemplateEngine());

        post("/addnutritionalinfo.post", (req, res) -> {
            String identifier = req.queryParams("serialNumber");
            float energy = Float.parseFloat(req.queryParams("energy"));
            float carbohydrate = Float.parseFloat(req.queryParams("energy"));
            float fat = Float.parseFloat(req.queryParams("fat"));
            float protein = Float.parseFloat(req.queryParams("protein"));

            try {
                nuDao.create(new NutritionalInfo(0, identifier, energy, carbohydrate, fat, protein));
            } catch (SQLException e) {
                res.redirect("/fail?msg" + e.getMessage());
            }
            res.redirect("/");

            return "";
        });

        post("/addserving.post", (req, res) -> {
            String serialNumber = req.queryParams("serialNumber");
            float mass = Float.parseFloat(req.queryParams("mass"));
            User user = (User) req.session().attribute("user");

            seDao.createServing(new Serving(0, user.getUuid(), user, serialNumber, null, null, mass, new Timestamp(System.currentTimeMillis())));

            res.redirect("/");

            return "";
        });

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            LoginResult checkUser = Service.checkUser(new User(null, username, password, null), uDao);

            if (checkUser.getUser() == null) {
                res.redirect("fail?msg=" + checkUser.getError());
                halt();
            }

            req.session(true).attribute("user", checkUser.getUser());
            res.redirect(req.queryParams("redirect"));
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
            String expirationstr = req.queryParams("expiration");
            Timestamp expiration = null;
            System.out.println(expirationstr);
            if (!expirationstr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate time = LocalDate.parse(expirationstr, formatter);
                expiration = Timestamp.valueOf(time.atStartOfDay());
                System.out.println(expiration.toString());
            }

            List<Tag> tags = ItemHelper.parseTags(req, uuid);

            Item item = new Item(uuid, name, serialNumber, location, new Timestamp(System.currentTimeMillis()), expiration, tags, type);
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

    private void checkLogin(Request req, Response res, String redirect) {
        User user = (User) req.session().attribute("user");

        if (user == null) {
            res.redirect("/login?redirect=" + redirect);
            halt();
        } else if (req.params(":username") != null && !user.getUsername().equals(req.params(":username"))) {
            res.redirect("/fail?msg=access_denied");
            halt();
        }
    }
}
