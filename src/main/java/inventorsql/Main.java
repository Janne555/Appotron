/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorsql;

import java.io.File;
import sql.db.Database;
import sql.db.DatabaseCreator;
import sql.db.Testdata;
import storables.Item;
import sql.db.ItemDao;

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

//        for (Item i : new ItemDao(database).findAll()) {
//            System.out.println(i);
//        }
        System.out.println("löydä yksi");
        Item findOne = new ItemDao(database).findOne("9bda8daf-5921-48c7-99ed-ed4a7ca0f511");
        System.out.println(findOne);

        System.out.println("löydä yksi");
        Item asd = new ItemDao(database).findOne("33e3eaaf-229b-46e6-85ac-281cd6c26eb8");
        System.out.println(asd);
    }
}
