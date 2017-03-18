/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import storables.Item;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import sql.daos.BugReportDao;
import sql.daos.MealComponentDao;
import sql.daos.ItemDao;
import static util.JsonUtil.json;
import sql.daos.ListItemDao;
import sql.daos.MealDao;
import sql.daos.NutritionalInfoDao;
import sql.daos.ServingDao;
import sql.daos.SessionControlDao;
import util.Param;
import sql.daos.ShoppingListDao;
import sql.daos.ItemInfoTagDao;
import sql.daos.UserDao;
import sql.db.Database;
import sql.db.LoginResult;
import storables.NutritionalInfo;
import storables.Serving;
import util.Type;
import storables.ShoppingList;
import storables.Tag;
import storables.User;
import util.Service;
import storables.*;
import util.PasswordUtil;

/**
 *
 * @author Janne
 */
public class WebMethods {

    ItemDao itemDao;
    ItemInfoTagDao tagDao;
    ListItemDao liDao;
    ShoppingListDao slDao;
    UserDao uDao;
    ServingDao seDao;
    NutritionalInfoDao nuDao;
    MealDao meDao;
    MealComponentDao ingDao;
    SessionControlDao secDao;
    BugReportDao buDao;

    private Database adminDb;

    public WebMethods(Database db) {
        this.itemDao = new ItemDao();
        this.tagDao = new ItemInfoTagDao(db);
        this.liDao = new ListItemDao(db);
        this.slDao = new ShoppingListDao(db);
        this.uDao = new UserDao();
        this.seDao = new ServingDao(db);
        this.nuDao = new NutritionalInfoDao(db);
        this.meDao = new MealDao(db);
        this.ingDao = new MealComponentDao(db);
        this.secDao = new SessionControlDao(db);
        this.buDao = new BugReportDao(db);
        this.adminDb = db;
        setupRoutes();
    }

    private void setupRoutes() {

        get("/login", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));
            map.put("redirect", req.queryParams("redirect"));
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            User user = uDao.findOne(username, adminDb);
            if (user == null) {
                halt();
                return "";
            } else if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                halt();
                return "";
            }

            req.session(true).attribute("user", user);
            SessionControl sessionControl = new SessionControl(null, user.getId(), new Timestamp(System.currentTimeMillis()));

            sessionControl.genNewSessionId();
            secDao.createSession(sessionControl);
            res.cookie("sessioncontrolid", sessionControl.getSessionId());
            res.redirect(req.queryParams("redirect"));
            return "";
        });
        before("/login", (req, res) -> {
            if (req.session().attribute("user") != null) {
                halt("Already logged in");
            }
        });

        before("/*", (req, res) -> {
            if (req.cookies().containsKey("sessioncontrolid")) {
                String cookie = req.cookie("sessioncontrolid");
                User user = secDao.getUser(cookie);
                req.session(true).attribute("user", user);
            }

            if (req.session(true).attribute("user") == null && !req.pathInfo().equals("/login")) {
                res.redirect("/login");
            } else if (req.session(true).attribute("user") != null && req.pathInfo().equals("/login")) {
                res.redirect("/fail?msg=alreadyloggedin");
            }
        });

    }

//
//        get("/", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//
//            map.put("items", itemDao.getExpiring(5));
//            ShoppingList sl = slDao.findOne(0);
//
//            if (sl != null) {
//                map.put("listitems", sl.getListItems());
//            }
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
////        get("/view", (req, res) -> {
////            HashMap map = new HashMap<>();
////            map.put("user", req.session().attribute("user"));
////            return new ModelAndView(map, "view");
////        }, new ThymeleafTemplateEngine());
//        get("/bugreport", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            return new ModelAndView(map, "bugreport");
//        }, new ThymeleafTemplateEngine());
//
//        get("/view", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//
//            Type type = Type.getType(req.queryParams("type"));
//            String uuid = req.queryParams("uuid");
//
//            if (type == Type.ITEM) {
//                Item item = itemDao.findOne(uuid);
//                map.put("item", item);
//                map.put("title", item.getName());
//                map.put("isItem", true);
//            } else if (type == Type.MEAL) {
//                Meal meal = meDao.findOne(uuid);
//                map.put("item", meal);
//                map.put("title", meal.getName());
//                map.put("isMeal", true);
//            }
//
//            map.put("type", type.getType());
//            return new ModelAndView(map, "view");
//        }, new ThymeleafTemplateEngine());
//
//        get("/search", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            String[] split = req.queryParams("query").split(" ");
//            List<Item> items = itemDao.search(split);
//            List<Meal> meals = meDao.search(split);
//            List<SearchResult> search = new ArrayList<>();
//            items.forEach((it) -> {
//                search.add((SearchResult) it);
//            });
//            meals.forEach((meal) -> {
//                search.add((SearchResult) meal);
//            });
//            map.put("items", search);
//
//            return new ModelAndView(map, "list");
//        }, new ThymeleafTemplateEngine());
//
//        get("/testing", (req, res) -> {
//            Meal m = meDao.findAll().get(0);
//            HashMap map = new HashMap<>();
//            map.put("name", m.getName());
//            map.put("energy", m.getEnergy());
//            map.put("carbs", m.getCarbohydrate());
//            map.put("protein", m.getProtein());
//            map.put("fat", m.getFat());
//            return map;
//        }, json());
//
//        get("/addmeal", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            String serial = req.queryParams("serial");
//
//            //in case of return link from barcode reader app
//            if (serial != null) {
//                map.put("serialfield", serial);
//            } else {
//                map.put("serialfield", "");
//            }
//
//            List<String> list = new ArrayList<>();
//            map.put("ingredients", list);
//
//            return new ModelAndView(map, "addmeal");
//        }, new ThymeleafTemplateEngine());
//
//        get("/ingredients.get", (req, res) -> {
//            System.out.println("received request for ingredients: " + req.queryParams("param"));
//            String param = req.queryParams("param");
//
//            List<Item> list = itemDao.search("foodstuff", param);
//            List<String> names = new ArrayList<>();
//
//            for (Item it : list) {
//                System.out.println(it);
//            }
//            return list;
//        }, json());
//
////        get("/meals.get", (req, res) -> {
////            System.out.println("received request for meals: " + req.queryParams("param"));
////            String param = req.queryParams("param");
////
////            List<Meal> search = meDao.search(param);
////
////            return search;
////        }, json());
//        get("/addnutritionalinfo", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            String serial = req.queryParams("serial");
//
//            //in case of return link from barcode reader app
//            if (serial != null) {
//                map.put("serialfield", serial);
//            } else {
//                map.put("serialfield", "");
//            }
//
//            return new ModelAndView(map, "addnutritionalinfo");
//        }, new ThymeleafTemplateEngine());
//
//        get("/mealdiary", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            checkLogin(req, res, "/mealdiary");
//            List<Serving> findTodaysByUser = seDao.findTodaysByUser((User) req.session().attribute("user"));
//            map.put("servings", findTodaysByUser);
//            float energy = 0;
//            float protein = 0;
//            float carbs = 0;
//            float fat = 0;
//
//            for (Serving s : findTodaysByUser) {
//                energy += s.getEnergy();
//                protein += s.getProtein();
//                carbs += s.getCarbohydrate();
//                fat += s.getFat();
//            }
//
//            map.put("energy", energy + " kcal");
//            map.put("protein", protein + " g");
//            map.put("carbs", carbs + " g");
//            map.put("fat", fat + " g");
//
//            return new ModelAndView(map, "mealdiary");
//        }, new ThymeleafTemplateEngine());
//
//        get("/logout", (req, res) -> {
//            HashMap map = new HashMap();
//            req.session().removeAttribute("user");
//            if (req.cookies().containsKey("sessioncontrolid")) {
//                req.cookies().remove("sessioncontrolid");
//                secDao.deleteSession(req.cookie("sessioncontrolid"));
//            }
//            res.redirect("/");
//            return new ModelAndView(map, "login");
//        });
//        before("/logout", (req, res) -> {
//            if (req.session().attribute("user") == null) {
//                halt("No user logged in");
//            }
//        });
//
//        get("/expiring.get", (req, res) -> {
//            System.out.println("received request for expiring foodstuffs");
//            return itemDao.getExpiring(5);
//        }, json());
//
//        get("/tags.get", (req, res) -> {
//            System.out.println("received request for tags: " + req.queryParams("param"));
//            List<Tag> list = tagDao.findAllByIdentifier(req.queryParams("param"));
//            for (Tag t : list) {
//                System.out.println(t);
//            }
//            return list;
//        }, json());
//
//        get("/additem", (req, res) -> {
//            HashMap map = new HashMap();
//            map.put("user", req.session().attribute("user"));
//            map.put("locations", itemDao.getLocations());
//            String name = "";
//            String serialNumber = "";
//            if (!req.queryParams().isEmpty()) {
//                name += req.queryParams("name");
//                serialNumber += req.queryParams("serial");
//            }
//            map.put("namefield", name);
//            map.put("serialfield", serialNumber);
//            return new ModelAndView(map, "additem");
//        }, new ThymeleafTemplateEngine());
//
//        get("/fail", (req, res) -> {
//            HashMap map = new HashMap();
//            map.put("user", req.session().attribute("user"));
//            map.put("msg", req.queryParams("msg"));
//            return new ModelAndView(map, "fail");
//        }, new ThymeleafTemplateEngine());
//
//        get("/tags", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("user", req.session().attribute("user"));
//            map.put("tags", tagDao.findAll());
//            return new ModelAndView(map, "dropdown");
//        }, new ThymeleafTemplateEngine());
//
//        get("/login", (req, res) -> {
//            HashMap map = new HashMap();
//            map.put("user", req.session().attribute("user"));
//            map.put("redirect", req.queryParams("redirect"));
//            return new ModelAndView(map, "login");
//        }, new ThymeleafTemplateEngine());
//
//        get("/profile/:username", (req, res) -> {
//            HashMap map = new HashMap();
//            map.put("user", req.session().attribute("user"));
//
//            User user = (User) req.session().attribute("user");
//
//            checkLogin(req, res, "/profile/" + user.getUsername());
//
//            return new ModelAndView(map, "profile");
//        }, new ThymeleafTemplateEngine());
//
//        get("/addserving", (req, res) -> {
//            HashMap map = new HashMap();
//            map.put("user", req.session().attribute("user"));
//
//            checkLogin(req, res, "/addserving");
//
//            return new ModelAndView(map, "addserving");
//        }, new ThymeleafTemplateEngine());
//
//        post("/addmeal.post", (req, res) -> {
//            String name = req.queryParams("name");
//            String type = req.queryParams("type");
//            List<Ingredient> ingredients = new ArrayList<>();
//
//            float totalMass = 0;
//
//            for (String s : req.queryParamsValues("ingredients[]")) {
//                s = s.substring(5);
//                String massStr = s.substring(0, s.indexOf(","));
//                float mass = Float.parseFloat(massStr);
//                totalMass += mass;
//                s = s.substring(s.indexOf(":") + 1);
//                String serialNumber = s;
//                Item item = itemDao.findOneBySerial(serialNumber);
//                NutritionalInfo nuInfo = nuDao.findOne(serialNumber);
//                ingredients.add(new Ingredient(null, null, mass, item, nuInfo));
//            }
//
//            String uuid = UUID.randomUUID().toString().substring(0, 11);
//            for (Ingredient i : ingredients) {
//                float percentage = i.getPercentage() / totalMass;
//                i.setPercentage(percentage);
//                i.setItemId(i.getItem().getSerialNumber());
//                i.setMealId(uuid);
//            }
//
//            Meal meal = new Meal(uuid, name, type, ingredients.toArray());
//
////            res.redirect("/");
//            return "";
//        });
//
////        post("/", (req, res) -> {
////            return "";
////        });
//
//
//        post("/bugreport.post", (req, res) -> {
//            User user = (User) req.session().attribute("user");
//            String subject = req.queryParams("subject");
//            System.out.println(subject);
//            String description = req.queryParams("description");
//            Timestamp date = new Timestamp(System.currentTimeMillis());
//            
//            buDao.create(new BugReport(user.getUuid(), user, subject, description, date));
//            
//            res.redirect("/");
//            
//            return "";
//        });
//        
//        post("/addnutritionalinfo.post", (req, res) -> {
//            String identifier = req.queryParams("serialNumber");
//            float energy = Float.parseFloat(req.queryParams("energy"));
//            float carbohydrate = Float.parseFloat(req.queryParams("energy"));
//            float fat = Float.parseFloat(req.queryParams("fat"));
//            float protein = Float.parseFloat(req.queryParams("protein"));
//
//            try {
//                nuDao.create(new NutritionalInfo(identifier, energy, carbohydrate, fat, protein));
//            } catch (SQLException e) {
//                res.redirect("/fail?msg" + e.getMessage());
//            }
//            res.redirect("/");
//
//            return "";
//        });
//
//        post("/addserving.post", (req, res) -> {
//            String serialNumber = req.queryParams("serialNumber");
//            float mass = Float.parseFloat(req.queryParams("mass"));
//            User user = (User) req.session().attribute("user");
//
//            seDao.createServing(new Serving(0, user.getUuid(), user, serialNumber, null, mass, new Timestamp(System.currentTimeMillis())));
//
//            res.redirect("/");
//
//            return "";
//        });
//
//        post("/login", (req, res) -> {
//            String username = req.queryParams("username");
//            String password = req.queryParams("password");
//
//            LoginResult checkUser = Service.checkUser(new User(null, username, password, null), uDao);
//
//            if (checkUser.getUser() == null) {
//                res.redirect("fail?msg=" + checkUser.getError());
//                halt();
//            }
//
//            req.session(true).attribute("user", checkUser.getUser());
//            SessionControl sessionControl = new SessionControl(null, checkUser.getUser().getUuid(), new Timestamp(System.currentTimeMillis()));
//            sessionControl.genNewSessionId();
//            secDao.createSession(sessionControl);
//            res.cookie("sessioncontrolid", sessionControl.getSessionId());
//            res.redirect(req.queryParams("redirect"));
//            return "";
//        });
//        before("/login", (req, res) -> {
//            if (req.session().attribute("user") != null) {
//                halt("Already logged in");
//            }
//        });
//
//        post("/additem.post", (req, res) -> {
//            for (String s : req.queryParams()) {
//                System.out.println(s);
//            }
//            String name = req.queryParams("name");
//            Type type = Type.getType(req.queryParams("type"));
//            String serialNumber = req.queryParams("serialNumber");
//            String uuid = UUID.randomUUID().toString();
//            String location = req.queryParams("location");
//            String expirationstr = req.queryParams("expiration");
//            Timestamp expiration = null;
//            System.out.println(expirationstr);
//            if (!expirationstr.isEmpty()) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                LocalDate time = LocalDate.parse(expirationstr, formatter);
//                expiration = Timestamp.valueOf(time.atStartOfDay());
//                System.out.println(expiration.toString());
//            }
//
//            List<Tag> tags = ItemHelper.parseTags(req, uuid);
//
//            Item item = new Item(uuid, name, serialNumber, location, new Timestamp(System.currentTimeMillis()), expiration, tags, type);
//            System.out.println(item);
//
//            try {
//                itemDao.create(item);
//            } catch (SQLException ex) {
//                res.redirect("/fail?msg=" + ex.toString());
//            }
//            res.redirect("/");
//            return "ok";
//        });
//
//    }
//
//    private void checkLogin(Request req, Response res, String redirect) {
//        User user = (User) req.session().attribute("user");
//
//        if (user == null) {
//            res.redirect("/login?redirect=" + redirect);
//            halt();
//        } else if (req.params(":username") != null && !user.getUsername().equals(req.params(":username"))) {
//            res.redirect("/fail?msg=access_denied");
//            halt();
//        }
//    }
}
