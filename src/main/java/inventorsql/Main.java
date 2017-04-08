/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import client.WebMethods;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import sql.daos.BugReportDao;
import sql.daos.FoodstuffDao;
import sql.daos.IngredientDao;
import sql.daos.MealComponentDao;
import sql.daos.MealDao;
import sql.daos.RecipeDao;
import sql.daos.SessionControlDao;
import sql.daos.UserDao;
import sql.db.Database;
import sql.db.Testdata;
import storables.*;
import storables.MealComponent;
import storables.User;
import util.PasswordUtil;
import util.Type;

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
        InputStream input;

        List<String> makeTables = new ArrayList<>();
        File f = new File("createtables.sql");
        Scanner r = new Scanner(f).useDelimiter(Pattern.compile("^\\s*$", Pattern.MULTILINE));
        while (r.hasNext()) {
            makeTables.add(r.next());
        }

        try {
            input = new FileInputStream("application.configuration");

            System.getProperties().load(input);

            input.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Database database = new Database("org.postgresql.Driver",
                System.getProperties().getProperty("postgre_address"),
                System.getProperties().getProperty("postgre_user"),
                System.getProperties().getProperty("postgre_password"));

        if (System.getProperties().getProperty("standalone").equals("true")) {
            String[] strings = {"globalreference",
                "foodstuffmeta",
                "bookmeta",
                "item",
                "permission",
                "person",
                "sessioncontrol",
                "bugreport",
                "meal",
                "mealcomponent",
                "recipe",
                "ingredient"};

            for (String s : strings) {
                try {
                    database.update("drop table " + s + " cascade", false);
                } catch (Exception e) {

                }
            }
        }

        if (System.getProperties().getProperty("standalone").equals("true")) {
            for (String s : makeTables) {
                database.update(s, false);
            }
            UserDao udao = new UserDao(database);
            udao.store(new User("jannetar", "janne", PasswordUtil.hashPassword("salis"), null, null));
        }

        if (System.getProperties().getProperty("insert_fineli_data").equals("true")) {
            BufferedReader names = new BufferedReader(new FileReader("foodname_FI.csv"));
            String line;
            HashMap<Integer, Foodstuff> map = new HashMap<>();
            while ((line = names.readLine()) != null) {
                String[] split = line.split(";");
                int id = Integer.parseInt(split[0]);
                String name = split[1];
                name = name.replace("%ae%", "ä");
                name = name.replace("%oe%", "ö");
                Foodstuff foodstuff = new Foodstuff(name, "FINELI", "FINELI", null, 0, 0, 0, 0, 0, 0, 0, null, null);
                map.put(id, foodstuff);
            }

            BufferedReader values = new BufferedReader(new FileReader("component_value.csv"));
            while ((line = values.readLine()) != null) {
                String[] split = line.split(";");
                if (split[2].isEmpty()) {
                    continue;
                }
                int id = Integer.parseInt(split[0]);
                Foodstuff get = map.remove(id);
                NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                Number number = format.parse(split[2]);
                float floatValue = number.floatValue();
                switch (split[1]) {
                    case "ENERC":
                        get.setCalories((float) (floatValue * 0.239005736 / 100));
                        break;

                    case "CHOAVL":
                        get.setCarbohydrate(floatValue / 100);
                        break;

                    case "FAT":
                        get.setFat(floatValue / 100);
                        break;

                    case "PROT":
                        get.setProtein(floatValue / 100);
                        break;
                }
                map.put(id, get);
            }
            float counter = 0;
            float length = map.size();
            int previous = 0;
            System.out.println("Inserting fineli data");
            for (Foodstuff foo : map.values()) {
                try {
                    int update = database.update("INSERT INTO globalreference(name, identifier, type) VALUES(?,?,?)", true, foo.getName(), foo.getIdentifier(), "foodstuff");
                    foo.setGlobalReferenceId(update);
                    database.update("INSERT INTO foodstuffmeta(globalreference_id, producer, calories, carbohydrate, fat, protein) VALUES(?,?,?,?,?,?)", false,
                            foo.getGlobalReferenceId(), foo.getProducer(), foo.getCalories(), foo.getCarbohydrate(), foo.getFat(), foo.getProtein());
                } catch (Exception e) {

                }
                int percentage = (int) (counter / length * 100);
                if (percentage > previous) {
                    System.out.println(percentage + "%");
                    previous = percentage;
                }
                counter++;
            }

        }
        
//        UserDao userDao = new UserDao(database);
//        MealDao mealDao = new MealDao(database);
//        MealComponentDao mealComponentDao = new MealComponentDao(database);
//        SessionControlDao sessionControlDao = new SessionControlDao(database);
//        BugReportDao bugReportDao = new BugReportDao(database);
//        RecipeDao recipeDao = new RecipeDao(database);
//        IngredientDao ingredientDao = new IngredientDao(database);
//        FoodstuffDao foodstuffDao = new FoodstuffDao(database);
//        
//        for (int i = 1; i < 34; i++) {
//            Foodstuff food = foodstuffDao.findOne(i);
//            Meal meal = new Meal(0, userDao.findByName("janne"), new Timestamp(System.currentTimeMillis()), null);
//            meal = mealDao.store(meal);
//            MealComponent mealComponent = new MealComponent(0, meal.getId(), i * 10, food);
//            mealComponentDao.store(mealComponent);
//        }
        
        new WebMethods(database);
    }
}
