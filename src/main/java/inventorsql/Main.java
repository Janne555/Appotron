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

        if (System.getProperties().getProperty("formatDatabase").equals("true")) {
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

        for (String s : makeTables) {
            try {
                database.update(s, false);
            } catch (SQLException e) {
                System.out.println("Update command \"" + s + "\" failed");
            }
        }

        if (System.getProperties().getProperty("insertDefaultUser").equals("true")) {
            UserDao udao = new UserDao(database);
            udao.store(new User(System.getProperties().getProperty("defaultUserIdentifier"), System.getProperties().getProperty("defaultUserName"), PasswordUtil.hashPassword(System.getProperties().getProperty("defaultUserPassword")), null, null));
        }

        new WebMethods(database);
    }
}
