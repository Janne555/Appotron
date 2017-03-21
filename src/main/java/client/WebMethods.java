/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import storables.*;
import sql.daos.*;
import sql.db.Database;
import sql.db.LoginResult;
import static util.JsonUtil.json;
import util.PasswordUtil;
import util.Service;

/**
 *
 * @author Janne
 */
public class WebMethods {

    ItemDao itemDao;
    ItemInfoDao infoDao;
    ItemInfoTagDao infoTagDao;
    ItemSpecificTagDao specTagdao;
    ListItemDao liDao;
    ShoppingListDao slDao;
    UserDao uDao;
    NutritionalInfoDao nutDao;
    MealDao meDao;
    MealComponentDao ingDao;
    SessionControlDao secDao;
    BugReportDao bugDao;

    private Database db;

    public WebMethods(Database db) {
        this.itemDao = new ItemDao(db);
        this.infoDao = new ItemInfoDao(db);
        this.infoTagDao = new ItemInfoTagDao(db);
        this.liDao = new ListItemDao(db);
        this.slDao = new ShoppingListDao(db);
        this.uDao = new UserDao(db);
        this.nutDao = new NutritionalInfoDao(db);
        this.meDao = new MealDao(db);
        this.ingDao = new MealComponentDao(db);
        this.secDao = new SessionControlDao(db);
        this.bugDao = new BugReportDao(db);
        this.specTagdao = new ItemSpecificTagDao(db);
        this.db = db;
        setupRoutes();
    }

    private void setupRoutes() {
        /**
         * filter for all requests
         */
        before("/*", (req, res) -> {
            if (req.cookies().containsKey("sessioncontrolid")) {
                String cookie = req.cookie("sessioncontrolid");
                User validUser = secDao.getValidUser(cookie);
                req.session(true).attribute("user", validUser);
            }

            if (req.session(true).attribute("user") == null && !req.pathInfo().equals("/login")) {
                res.redirect("/login");
            } else if (req.session(true).attribute("user") != null && req.pathInfo().equals("/login")) {
                res.redirect("/fail?msg=alreadyloggedin");
            }
        });

        /**
         * login page
         */
        get("/login", (req, res) -> {
            HashMap map = new HashMap();
            map.put("user", req.session().attribute("user"));
            map.put("redirect", req.queryParams("redirect"));
            return new ModelAndView(map, "login");
        }, new ThymeleafTemplateEngine());

        /**
         * login post
         */
        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            User user = uDao.findByName(username);
            if (user == null) {
                halt();
                return "";
            } else if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
                halt();
                return "";
            }

            req.session(true).attribute("user", user);
            SessionControl sessionControl = new SessionControl(null, user.getId(), new Timestamp(System.currentTimeMillis()));

            sessionControl.genSessionId();
            secDao.store(sessionControl);
            res.cookie("sessioncontrolid", sessionControl.getSessionId());
            res.redirect(req.queryParams("redirect"));
            return "";
        });

        /**
         * filter for login page
         */
        before("/login", (req, res) -> {
            if (req.session().attribute("user") != null) {
                halt("Already logged in");
            }
        });

        /**
         * index page
         */
        get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            map.put("items", itemDao.getExpiring(user, 5));
            ShoppingList sl = slDao.findOne(0);

            if (sl != null) {
                map.put("listitems", sl.getListItems());
            }

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

        /**
         * bug reporting page
         */
        get("/bugreport", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            return new ModelAndView(map, "bugreport");
        }, new ThymeleafTemplateEngine());

        /**
         * page for viewing items
         *
         * expects id and type as attributes example
         *
         * /view?id=something&type=something
         */
        get("/view", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            String type = req.queryParams("type");
            int id = 0;

            try {
                id = Integer.parseInt(req.queryParams("id"));
            } catch (NumberFormatException e) {
                halt();
            }

            if (type.equals("item") || type.equals("foodstuff") || type.equals("book")) {
                Item item = itemDao.findOne(id, user);
                map.put("item", item);
                map.put("title", item.getName());
                map.put("isItem", true);
            } else if (type.equals("meal")) {
                Meal meal = meDao.findOne(user, id);
                map.put("item", meal);
                map.put("title", meal.getDate());
                map.put("isMeal", true);
            }

            map.put("type", type);
            return new ModelAndView(map, "view");
        }, new ThymeleafTemplateEngine());

        /**
         * searchresults page
         *
         * expects query as an attribute
         *
         * example /search?query=one&query=two&...&query=nth
         */
        get("/search", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            Object[] split = req.queryParams("query").split(" ");
            List<Item> items = itemDao.search(user, split);

            List<SearchResult> search = new ArrayList<>();
            items.forEach((it) -> {
                search.add((SearchResult) it);
            });

            map.put("items", search);

            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        /**
         * add meal page
         */
        get("/addmeal", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            return new ModelAndView(map, "addmeal");
        }, new ThymeleafTemplateEngine());

        /**
         * get request for foodstuffs
         *
         * expects query as an attribute
         *
         * example /addmealsearch.get?query=one
         */
        get("/addmealsearch.get", (req, res) -> {
            User user = (User) req.session().attribute("user");

            Object[] split = req.queryParams("param").split(" ");

            List<ItemInfo> list = infoDao.search(split);
            return list;
        }, json());

        /**
         * add nutritional info page
         *
         * expects id as an attribute
         *
         * example /addnutritionalinfo?id=something
         */
        get("/addnutritionalinfo", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            int id = Integer.parseInt(req.queryParams("id"));
            ItemInfo itemInfo = infoDao.findOne(id);
            map.put("iteminfo", itemInfo);

            return new ModelAndView(map, "addnutritionalinfo");
        }, new ThymeleafTemplateEngine());

        /**
         * meal diary page
         *
         * shows meals had that day
         *
         * can take attributes from and to as timedate-strings or today=true to
         * give results for today
         */
        get("/mealdiary", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            List<Meal> findAll = null;
            String today = req.queryParams("today");
            String from = req.queryParams("from");
            String to = req.queryParams("to");

            if (today.equals("true")) {
                Timestamp beginning = Timestamp.valueOf(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));
                Timestamp end = Timestamp.valueOf(LocalDateTime.now().withHour(23).withMinute(59).withSecond(59));
                findAll = meDao.findAll(user, beginning, end);
            } else if (from != null && to != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate fromDate = LocalDate.parse(from, formatter);
                LocalDate toDate = LocalDate.parse(to, formatter);
                Timestamp fromTimestamp = Timestamp.valueOf(fromDate.atStartOfDay());
                Timestamp toTimestamp = Timestamp.valueOf(toDate.atTime(23, 59, 59));
                findAll = meDao.findAll(user, fromTimestamp, toTimestamp);
            } else {
                findAll = meDao.findAll(user);
            }

            map.put("meals", findAll);
            float energy = 0;
            float protein = 0;
            float carbs = 0;
            float fat = 0;

            for (Meal m : findAll) {
                energy += m.getEnergy();
                protein += m.getProtein();
                carbs += m.getCarbohydrate();
                fat += m.getFat();
            }

            map.put("energy", energy + " cal");
            map.put("protein", protein + " g");
            map.put("carbs", carbs + " g");
            map.put("fat", fat + " g");

            return new ModelAndView(map, "mealdiary");
        }, new ThymeleafTemplateEngine());

        /**
         * logout page
         */
        get("/logout", (req, res) -> {
            HashMap map = new HashMap();
            req.session().removeAttribute("user");
            if (req.cookies().containsKey("sessioncontrolid")) {
                req.cookies().remove("sessioncontrolid");
                secDao.invalidateSession(req.cookie("sessioncontrolid"));
            }
            res.redirect("/");
            return new ModelAndView(map, "login");
        });

        /**
         * filter for logout page
         */
        before("/logout", (req, res) -> {
            if (req.session().attribute("user") == null) {
                halt("No user logged in");
            }
        });

        /**
         * returns json object of 5 expiring foodstuff
         */
        get("/expiring.get", (req, res) -> {
            User user = (User) req.session().attribute("user");
            return itemDao.getExpiring(user, 5);
        }, json());

        /**
         * returns json object of item specific tags
         */
        get("/tags.get", (req, res) -> {
            List<Tag> list = null;
            try {
                list = specTagdao.findAll(Integer.parseInt(req.queryParams("param")));
            } catch (NumberFormatException | SQLException e) {
                halt();
            }
            return list;
        }, json());

        /**
         * add item page
         *
         * if attributes name and serial are given the page will contain those
         * in their respective fields
         */
        get("/additem", (req, res) -> {
            HashMap map = new HashMap();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            map.put("locations", itemDao.getLocations(user));
            String name = "";
            String identifier = "";
            if (!req.queryParams().isEmpty()) {
                name += req.queryParams("name");
                identifier += req.queryParams("serial");
            }
            map.put("namefield", name);
            map.put("identifierfield", identifier);
            return new ModelAndView(map, "additem");
        }, new ThymeleafTemplateEngine());
        
        /**
         * fail page
         * 
         * in case of something going wrong
         */
        get("/fail", (req, res) -> {
            HashMap map = new HashMap();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            map.put("msg", req.queryParams("msg"));
            return new ModelAndView(map, "fail");
        }, new ThymeleafTemplateEngine());
        
        /**
         * profile page
         * 
         * takes username as an attribute
         * 
         * example /profile/user
         */
        get("/profile/:username", (req, res) -> {
            HashMap map = new HashMap();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            String username = req.params(":username");

            if (!user.getUsername().equals(username)) {
                res.redirect("/");
            }

            return new ModelAndView(map, "profile");
        }, new ThymeleafTemplateEngine());

//        post("/addmeal.post", (req, res) -> {
//            User user = (User) req.session().attribute("user");
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
        post("/bugreport.post", (req, res) -> {
            User user = (User) req.session().attribute("user");
            String subject = req.queryParams("subject");
            System.out.println(subject);
            String description = req.queryParams("description");
            Timestamp date = new Timestamp(System.currentTimeMillis());

            bugDao.create(new BugReport(user.getId(), user, subject, description, date));

            res.redirect("/");

            return "";
        });

        post("/addnutritionalinfo.post", (req, res) -> {
            try {
                int id = Integer.parseInt(req.queryParams("identifier"));
                float energy = Float.parseFloat(req.queryParams("energy"));
                float carbohydrate = Float.parseFloat(req.queryParams("energy"));
                float fat = Float.parseFloat(req.queryParams("fat"));
                float protein = Float.parseFloat(req.queryParams("protein"));
                nutDao.store(new NutritionalInfo(infoDao.findOne(id), energy, carbohydrate, fat, protein));
            } catch (NumberFormatException | SQLException e) {
                res.redirect("/fail?msg" + e.getMessage());
            }
            res.redirect("/");

            return "";
        });

        post("/login", (req, res) -> {
            String username = req.queryParams("username");
            String password = req.queryParams("password");

            LoginResult checkUser = Service.checkUser(new User(null, username, password, null, null), uDao);

            if (checkUser.getUser() == null) {
                res.redirect("fail?msg=" + checkUser.getError());
                halt();
            }

            req.session(true).attribute("user", checkUser.getUser());
            SessionControl sessionControl = new SessionControl(null, checkUser.getUser().getId(), new Timestamp(System.currentTimeMillis()));
            sessionControl.genSessionId();
            secDao.store(sessionControl);
            res.cookie("sessioncontrolid", sessionControl.getSessionId());
            res.redirect(req.queryParams("redirect"));
            return "";
        });
        before("/login", (req, res) -> {
            if (req.session().attribute("user") != null) {
                halt("Already logged in");
            }
        });

        post("/additem.post", (req, res) -> {
            User user = (User) req.session().attribute("user");

            String name = req.queryParams("name");
            String type = req.queryParams("type");
            String identifier = req.queryParams("identifier");
            String location = req.queryParams("location");
            String expirationstr = req.queryParams("expiration");
            Timestamp expiration = null;
            if (!expirationstr.isEmpty()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate time = LocalDate.parse(expirationstr, formatter);
                expiration = Timestamp.valueOf(time.atStartOfDay());
            }

            ItemInfo itemInfo;
            if ((itemInfo = infoDao.findOne(name, identifier)) == null) {
                itemInfo = new ItemInfo(0, name, type, identifier, null);
                itemInfo = infoDao.store(itemInfo);
            }

            Item item = new Item(0, location, new Timestamp(System.currentTimeMillis()), expiration, null, itemInfo);
            itemDao.store(item, user);

            res.redirect("/view?id=" + item.getIdentifier() + "&type=item");
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
