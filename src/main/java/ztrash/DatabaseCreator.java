///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.db;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.Reader;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Janne
// */
//public class DatabaseCreator {
//    private String filename;
//    private Database db;
//    private List<String> tables;
//
//    public DatabaseCreator(String filename, Database db) throws FileNotFoundException, SQLException {
//        this.filename = filename;
//        this.db = db;
//        this.tables = new ArrayList<>();
//
//        parseJson();
//        createTables();
//    }
//
//    private void parseJson() throws FileNotFoundException {
//        //create a file object 
//        File file = new File(filename);
//        //create a reader from file object
//        Reader reader = new FileReader(file);
//        //parse a json object from file reader input
//        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
//        //get array of tables
//        JsonArray tableJsonArray = json.getAsJsonArray("tables");
//
//        //loop through array of tables
//        for (JsonElement table : tableJsonArray) {
//            String sql = "CREATE TABLE ";
//            //turn element into object
//            JsonObject tableJsonObj = table.getAsJsonObject();
//            //get the name of the table
//            String name = tableJsonObj.get("name").getAsString();
//            sql += name + "(";
//            //get columns as an array
//            JsonArray columns = tableJsonObj.getAsJsonArray("columns");
//            //loop through array of columns
//            for (JsonElement column : columns) {
//                //turn element into object
//                JsonObject columnJsonObj = column.getAsJsonObject();
//                //get a set of key value pairs as an iterator
//                Iterator<Map.Entry<String, JsonElement>> entrySet = columnJsonObj.entrySet().iterator();
//                //loop through iterator
//                while (entrySet.hasNext()) {
//                    //get a key value pair
//                    Map.Entry<String, JsonElement> next = entrySet.next();
//                    //get key from pair
//                    String valueName = next.getKey();
//                    if (valueName.contains("FOREIGN")) {
//                        valueName = "FOREIGN";
//                    }
//                    //get value from pair
//                    String valueType = next.getValue().getAsString();
//                    sql += valueName + " " + valueType + ", ";
//                }
//            }
//            //add completed table object to list of table objects
//            sql = sql.substring(0, sql.length() - 2) + ")";
//            tables.add(sql);
//        }
//    }
//
//    private void createTables() {
//        for (String t : tables) {
//            try {
//                db.update(t);
//                System.out.println("Table created succesfully: " + t);
//            } catch (SQLException ex) {
//                if (ex.getMessage().contains("already exists")) {
//                    System.out.println("Table already exists: " + t);
//                }
//                else {
//                    System.out.println(ex.getMessage());
//                    System.out.println(t);
//                }
//            }
//        }
//    }
//}
