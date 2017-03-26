/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import storables.*;
import sql.daos.*;
import sql.db.Database;
import static util.JsonUtil.json;
import util.PasswordUtil;

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
    MealComponentDao mecoDao;
    SessionControlDao secDao;
    BugReportDao bugDao;
    IngredientDao ingDao;
    RecipeDao recDao;

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
        this.mecoDao = new MealComponentDao(db);
        this.secDao = new SessionControlDao(db);
        this.bugDao = new BugReportDao(db);
        this.specTagdao = new ItemSpecificTagDao(db);
        this.recDao = new RecipeDao(db);
        this.ingDao = new IngredientDao(db);
        this.db = db;
        setupRoutes();
    }

    private void setupRoutes() {
        userRoutes();
        addItemRoutes();
        mealRoutes();
        searchRoutes();
        viewRoutes();
        nutritionalInfoRoutes();
        frontPageRoutes();
        editRoutes();
        recipeRoutes();

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
         * bug reporting page
         */
        get("/bugreport", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            return new ModelAndView(map, "bugreport");
        }, new ThymeleafTemplateEngine());

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

    }

    private void addItemRoutes() {
        /**
         * add item page
         */
        get("/additem", (req, res) -> {
            HashMap map = new HashMap();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            String type = req.queryParams("type");
            if (null == type) {
                res.redirect("/");
                halt();
            } else {
                switch (type) {
                    case "item":
                        map.put("title", "Add Item");
                        map.put("labelname", "Name");
                        map.put("labelidentifier", "Serial Number");
                        map.put("item", true);
                        break;
                    case "foodstuff":
                        map.put("title", "Add Foodstuff");
                        map.put("labelname", "Name");
                        map.put("labelidentifier", "Serial Number");
                        map.put("foodstuff", true);
                        break;
                    case "book":
                        map.put("title", "Add Book");
                        map.put("labelname", "Title");
                        map.put("labelidentifier", "ISBN");
                        map.put("book", true);
                        map.put("genrelist", infoTagDao.getGenres());
                        break;
                    default:
                        res.redirect("/");
                        break;
                }
            }

            map.put("action", "/additem.post");
            map.put("type", type);
            map.put("locations", itemDao.getLocations(user));
            return new ModelAndView(map, "additem");
        }, new ThymeleafTemplateEngine());

        post("/additem.post", (req, res) -> {
            User user = (User) req.session().attribute("user");

            String name = req.queryParams("name");
            String type = req.queryParams("type");
            String identifier = req.queryParams("identifier");
            String location = req.queryParams("location");
            int copies = 1;
            Timestamp expiration = null;
            ItemInfo itemInfo;

            if ((itemInfo = infoDao.findOne(name, identifier)) == null) {
                itemInfo = new ItemInfo(0, name, type, identifier, null);
                itemInfo = infoDao.store(itemInfo);
            }

            switch (type) {
                case "book":
                    Tag author = new Tag(0, itemInfo.getId(), "author", req.queryParams("author"));
                    Tag publisher = new Tag(0, itemInfo.getId(), "publisher", req.queryParams("publisher"));
                    Tag pubYear = new Tag(0, itemInfo.getId(), "publishing year", req.queryParams("year"));
                    Tag genre = new Tag(0, itemInfo.getId(), "genre", req.queryParams("genre"));
                    infoTagDao.store(author, publisher, pubYear, genre);
                    break;

                case "foodstuff":
                    String expirationstr = req.queryParams("expiration");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate time = LocalDate.parse(expirationstr, formatter);
                    expiration = Timestamp.valueOf(time.atStartOfDay());
                    Tag producer = new Tag(0, itemInfo.getId(), "producer", req.queryParams("producer"));
                    infoTagDao.store(producer);
                    break;

                case "item":
                    break;
            }

            String copystr = req.queryParams("copies");
            if (copystr != null) {
                copies = Integer.parseInt(req.queryParams("copies"));
            }

            Item item = null;
            for (int i = 0; i < copies; i++) {
                item = new Item(0, location, new Timestamp(System.currentTimeMillis()), expiration, null, itemInfo);
                itemDao.store(item, user);
            }

            res.redirect("/view?id=" + item.getId() + "&type=" + type);
            return "ok";
        });
    }

    private void userRoutes() {

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

    }

    private void mealRoutes() {
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

        post("/addmeal.post", (req, res) -> {
            User user = (User) req.session().attribute("user");
            for (String s : req.queryParamsValues("mealcomponents")) {
                System.out.println(s);
            }

            Meal meal = new Meal(0, user, new Timestamp(System.currentTimeMillis()), null);
            meal = meDao.store(meal);

            List<MealComponent> components = new ArrayList<>();
            for (String s : req.queryParamsValues("mealcomponents")) {
                int id = Integer.parseInt(s);
                System.out.println(req.queryParams("massforid:" + s));
                float mass = Float.parseFloat(req.queryParams("massforid:" + s));
                MealComponent component = new MealComponent(0, meal.getId(), mass, infoDao.findOne(id), nutDao.findOneByItemInfoId(id));
                mecoDao.store(component);
            }

            res.redirect("/view?id=" + meal.getId() + "&type=meal");
            return "";
        });
    }

    private void searchRoutes() {

        /**
         * searchresults page
         *
         * expects query as an attribute
         *
         * example /search?query=one&query=two&...&query=nth
         */
        get("/search/all", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);
            String type = req.queryParams("type");
            switch (type) {
                case "foodstuff":
                    map.put("foodstuffs", itemDao.findAllByType(user, "foodstuff"));
                    map.put("foodstuff", true);
                    break;
                    
                case "book":
                    map.put("books", itemDao.findAllByType(user, "book"));
                    map.put("book", true);
                    break;
                    
                case "item":
                    map.put("items", itemDao.findAllByType(user, "item"));
                    map.put("item", true);
                    break;
                    
                case "recipe":
                    map.put("recipes", recDao.findAll());
                    map.put("recipe", true);
                    break;
                    
                default:
                    res.redirect("/");
                    halt();
            }

            return new ModelAndView(map, "list");
        }, new ThymeleafTemplateEngine());

        get("/search", (req, res) -> {
            if (req.queryParams("query") == null) {
                res.redirect("/");
                halt();
            }
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            Object[] searchTerms = req.queryParams("query").split(" ");
            List<Item> itemsResult = itemDao.search(user, searchTerms);
            List<Recipe> recipesResult = recDao.search(searchTerms);

            if (!itemsResult.isEmpty()) {
                List<Item> items = new ArrayList<>();
                List<Item> foodstuffs = new ArrayList<>();
                List<Item> books = new ArrayList<>();

                boolean item = false;
                boolean foodstuff = false;
                boolean book = false;

                for (Item i : itemsResult) {
                    switch (i.getType()) {
                        case "item":
                            items.add(i);
                            item = true;
                            break;

                        case "foodstuff":
                            foodstuffs.add(i);
                            foodstuff = true;
                            break;

                        case "book":
                            books.add(i);
                            book = true;
                            break;

                        default:
                    }

                    map.put("items", items);
                    map.put("item", item);
                    map.put("foodstuffs", foodstuffs);
                    map.put("foodstuff", foodstuff);
                    map.put("books", books);
                    map.put("book", book);
                }
            }
            
            if (!recipesResult.isEmpty()) {
                map.put("recipes", recipesResult);
                map.put("recipe", true);
            }

            return new ModelAndView(map, "list");
        },

                 new ThymeleafTemplateEngine()
        );
    }

    private void viewRoutes() {
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

            if (type != null && type.equals("meal")) {
                Meal meal = meDao.findOne(user, id);
                map.put("item", meal);
                map.put("isMeal", true);
                map.put("title", "Meal: " + meal.getDate());
                return new ModelAndView(map, "view");
            } else if (type != null && type.equals("recipe")) {
                Recipe recipe = recDao.findOne(id);
                map.put("item", recipe);
                map.put("isRecipe", true);
                map.put("title", recipe.getName());
            } else {
                Item item = itemDao.findOne(id, user);
                List<String> tags = new ArrayList<>();

                if (item != null) {
                    switch (item.getType()) {
                        case "item":
                            map.put("item", item);
                            map.put("title", item.getName());
                            map.put("isItem", true);
                            for (Tag t : item.getTags()) {
                                tags.add(t.getKey() + ": " + t.getValue());
                            }
                            map.put("tags", tags);
                            break;
                        case "foodstuff":
                            NutritionalInfo nutinfo = nutDao.findOneByItemInfoId(item.getItemInfo().getId());
                            if (nutinfo != null) {
                                map.put("nutritionalinfo", nutinfo);
                            }
                            map.put("item", item);
                            map.put("title", item.getName());
                            map.put("isItem", true);
                            map.put("isFoodstuff", true);
                            for (Tag t : item.getTags()) {
                                if (t.getKey().equals("producer")) {
                                    map.put("producer", t.getValue());
                                } else {
                                    tags.add(t.getKey() + ": " + t.getValue());
                                }
                            }
                            map.put("tags", tags);
                            break;

                        case "book":
                            map.put("item", item);
                            map.put("title", item.getName());
                            map.put("isBook", true);
                            for (Tag t : item.getTags()) {
                                switch (t.getKey()) {
                                    case "author":
                                        map.put("author", t.getValue());
                                        break;
                                    case "publisher":
                                        map.put("publisher", t.getValue());
                                        break;
                                    case "publishing year":
                                        map.put("year", t.getValue());
                                        break;
                                    case "genre":
                                        map.put("genre", t.getValue());
                                        break;
                                    default:
                                        tags.add(t.getKey() + ": " + t.getValue());
                                        break;
                                }
                            }
                            map.put("tags", tags);
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
            }
            map.put("type", type);
            return new ModelAndView(map, "view");
        }, new ThymeleafTemplateEngine());

    }

    private void nutritionalInfoRoutes() {
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
            int id = 0;
            try {
                id = Integer.parseInt(req.queryParams("id"));
            } catch (NumberFormatException e) {
                res.redirect("/");
                halt();
            }

            ItemInfo iteminfo = infoDao.findOne(id);

            if (nutDao.findOneByItemInfoId(id) != null || iteminfo == null) {
                res.redirect("/");
                halt();
            }

            map.put("title", "Add Nutritional Info for:");
            map.put("title2", iteminfo.getName() + ", Serial Number: " + iteminfo.getIdentifier());
            map.put("title3", "per 100 g");
            map.put("isAddNutritionalInfo", true);
            map.put("showNutritionalInfo", true);
            map.put("action", "/addnutritionalinfo.post");
            map.put("iteminfoid", id);
            return new ModelAndView(map, "additem");
        }, new ThymeleafTemplateEngine());

        post("/addnutritionalinfo.post", (req, res) -> {
            try {
                int id = Integer.parseInt(req.queryParams("iteminfoid"));
                float energy = Float.parseFloat(req.queryParams("energy"));
                float carbohydrate = Float.parseFloat(req.queryParams("carbohydrate"));
                float fat = Float.parseFloat(req.queryParams("fat"));
                float protein = Float.parseFloat(req.queryParams("protein"));
                if ((carbohydrate + fat + protein) > 100) {
                    throw new NumberFormatException("sum of components can't be above 100g");
                }
                nutDao.store(new NutritionalInfo(infoDao.findOne(id), energy, carbohydrate, fat, protein));
            } catch (NumberFormatException | SQLException e) {
                res.redirect("/fail?msg" + e.getMessage());
            }
            res.redirect("/");

            return "";
        });

        /**
         * a filter to make sure the iteminfo entry exists and no
         * nutritionalinfo is associated with it already
         */
        before("/nutritionalinfo.post", (req, res) -> {
            try {
                int id = Integer.parseInt(req.queryParams("iteminfoid"));
                if (infoDao.findOne(id) == null || nutDao.findOneByItemInfoId(id) != null) {
                    res.redirect("/");
                    halt();
                }
            } catch (NumberFormatException e) {
                res.redirect("/");
                halt();
            }
        });
    }

    private void frontPageRoutes() {

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
         * returns json object of 5 expiring foodstuff
         */
        get("/expiring.get", (req, res) -> {
            User user = (User) req.session().attribute("user");
            return itemDao.getExpiring(user, 5);
        }, json());
    }

    private void editRoutes() {
        get("/edit", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            try {
                int id = Integer.parseInt(req.queryParams("id"));
                Item item = itemDao.findOne(id, user);

                map.put("itemid", item.getId());
                map.put("type", item.getType());
                map.put("title", "Edit " + item.getType());
                map.put("name", item.getName());
                map.put("location", item.getLocation());
                map.put("identifier", item.getIdentifier());

                switch (item.getType()) {
                    case "book":
                        map.put("book", true);
                        map.put("author", item.getAuthor());
                        map.put("publisher", item.getPublisher());
                        map.put("year", item.getYear());
                        map.put("genre", item.getGenre());
                        map.put("labelname", "Title");
                        map.put("labelidentifier", "ISBN");
                        break;

                    case "foodstuff":
                        map.put("labelname", "Name");
                        map.put("labelidentifier", "Serial Number");
                        map.put("foodstuff", true);
                        map.put("producer", item.getProducer());
                        map.put("expiration", item.getExpirationString());
                        NutritionalInfo nutInfo = nutDao.findOneByItemInfoId(item.getItemInfo().getId());
                        if (nutInfo != null) {
                            map.put("showNutritionalInfo", true);
                            map.put("energy", nutInfo.getCalories());
                            map.put("carbohydrate", nutInfo.getCarbohydrate());
                            map.put("fat", nutInfo.getFat());
                            map.put("protein", nutInfo.getProtein());
                        }
                        break;

                    case "item":
                        map.put("labelname", "Name");
                        map.put("labelidentifier", "Serial Number");
                        map.put("item", true);
                        break;

                    default:
                        throw new AssertionError();
                }
            } catch (NumberFormatException | SQLException e) {
                res.redirect("/");
                halt();
            }
            map.put("action", "/edititem.post");
            return new ModelAndView(map, "additem");
        }, new ThymeleafTemplateEngine());

        post("/edititem.post", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            String itemId = req.queryParams("itemid");
            String name = req.queryParams("name");
            String identifier = req.queryParams("identifier");
            String location = req.queryParams("location");
            String producer = req.queryParams("producer");
            String expiration = req.queryParams("expiration");
            String author = req.queryParams("author");
            String publisher = req.queryParams("publisher");
            String year = req.queryParams("year");
            String genre = req.queryParams("genre");
            String energy = req.queryParams("energy");
            String carbohydrate = req.queryParams("carbohydrate");
            String fat = req.queryParams("fat");
            String protein = req.queryParams("protein");

            try {
                int itemIdInt = Integer.parseInt(itemId);
                Item item = itemDao.findOne(itemIdInt, user);
                item.setName(name);
                item.setIdentifier(identifier);
                item.setLocation(location);

                switch (item.getType()) {
                    case "foodstuff":
                        NutritionalInfo nutInfo = nutDao.findOneByItemInfoId(item.getItemInfo().getId());
                        if (nutInfo != null) {
                            nutInfo.setEnergy(Float.parseFloat(energy));
                            nutInfo.setCarbohydrates(Float.parseFloat(carbohydrate));
                            nutInfo.setFat(Float.parseFloat(fat));
                            nutInfo.setProtein(Float.parseFloat(protein));
                            nutDao.update(nutInfo);
                        }

                        item.getInfoTag("producer").setValue(producer);

                        itemDao.update(item, user);

                        break;

                    case "book":
                        item.getInfoTag("author").setValue(author);
                        item.getInfoTag("publisher").setValue(publisher);
                        item.getInfoTag("publishing year").setValue(year);
                        item.getInfoTag("genre").setValue(genre);

                        itemDao.update(item, user);

                        break;

                    case "item":
                        itemDao.update(item, user);
                        break;
                    default:
                        throw new AssertionError();
                }
            } catch (NumberFormatException | AccessDeniedException | SQLException e) {
                res.redirect("/fail?msg=" + e.toString());
                Logger
                        .getLogger(WebMethods.class
                                .getName()).log(Level.SEVERE, null, e);
                halt();
            }
            res.redirect("/view?id=" + itemId);
            return "";
        });
    }

    private void recipeRoutes() {
        get("/addrecipe", (req, res) -> {
            HashMap map = new HashMap<>();
            User user = (User) req.session().attribute("user");
            map.put("user", user);

            return new ModelAndView(map, "addrecipe");
        }, new ThymeleafTemplateEngine());

        post("/addrecipe.post", (req, res) -> {
            List<MealComponent> components = new ArrayList<>();
            User user = (User) req.session().attribute("user");

            String name = req.queryParams("recipename");
            String directions = req.queryParams("directions");
            String description = req.queryParams("description");
            String recipeType = req.queryParams("recipetype");

            Recipe recipe = new Recipe(0, name, directions, recipeType, description, null);
            recipe = recDao.store(recipe);

            for (String s : req.queryParamsValues("ingredients")) {
                int id = Integer.parseInt(s);
                float amount = Float.parseFloat(req.queryParams("amountfor:" + s));
                String unit = req.queryParams("unitfor:" + s);
                Ingredient ingredient = new Ingredient(0, recipe.getId(), amount, unit, infoDao.findOne(id), id, null);
                ingDao.store(ingredient);
            }
            res.redirect("/view?id=" + recipe.getId() + "&type=recipe");
            return "";
        });
    }

}
