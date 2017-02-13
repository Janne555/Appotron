/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Janne
 */
public class DatabaseInfo {
    private String databaseName;
    private String filename;
    private List<Table> tables;

    public DatabaseInfo(String filename) throws FileNotFoundException {
        this.filename = filename;
        //initiate the list of table objects
        this.tables = new ArrayList<>();

        init();
    }

    private void init() throws FileNotFoundException {
        //create a file object 
        File file = new File(filename);
        //create a reader from file object
        Reader reader = new FileReader(file);
        //parse a json object from file reader input
        JsonObject json = new JsonParser().parse(reader).getAsJsonObject();
        //get name of database
        databaseName = json.get("database").getAsString();
        //get array of tables
        JsonArray tableJsonArray = json.getAsJsonArray("tables");

        //loop through array of tables
        for (JsonElement table : tableJsonArray) {
            //turn element into object
            JsonObject tableJsonObj = table.getAsJsonObject();
            //get the name of the table
            String name = tableJsonObj.get("name").getAsString();
            //create a table object
            Table tableObj = new Table(name);
            //get columns as an array
            JsonArray columns = tableJsonObj.getAsJsonArray("columns");
            //loop through array of columns
            for (JsonElement column : columns) {
                //turn element into object
                JsonObject columnJsonObj = column.getAsJsonObject();
                //get a set of key value pairs as an iterator
                Iterator<Map.Entry<String, JsonElement>> entrySet = columnJsonObj.entrySet().iterator();
                //loop through iterator
                while (entrySet.hasNext()) {
                    //get a key value pair
                    Map.Entry<String, JsonElement> next = entrySet.next();
                    //get key from pair
                    String key = next.getKey();
                    //get value from pair
                    String value = next.getValue().getAsString();
                    //store key and value to table object's lists
                    tableObj.getColumnNames().add(key);
                    tableObj.getCommands().add(value);
                }
            }
            //add completed table object to list of table objects
            tables.add(tableObj);
        }
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getFilename() {
        return filename;
    }

    public List<Table> getTables() {
        return tables;
    }

}
