///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.db;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// *
// * @author Janne
// */
//public class TableCreator {
//    private String name;
//    private List<String> columnNames;
//    private List<String> commands;
//
//    public TableCreator(String name) {
//        this.name = name;
//        this.columnNames = new ArrayList<>();
//        this.commands = new ArrayList<>();
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public List<String> getColumnNames() {
//        return columnNames;
//    }
//
//    public List<String> getCommands() {
//        return commands;
//    }
//    
//    @Override
//    public String toString() {
//        String response = "Table name is: " + this.name +"\n"
//                + "Table contains the following columns: \n";
//        Iterator<String> it = commands.iterator();
//        for (String column : columnNames) {
//            response += column + "\t command: " + it.next() +"\n";
//        }
//        
//        return response;
//    }
//}
