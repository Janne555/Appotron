///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.db;
//
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// *
// * @author Janne
// */
//public class QueryBuilder {
//    private String columns;
//    private String table;
//    private String parameters;
//    private String query;
//    
//
//    public QueryBuilder(Enum... enumerators) {
//        this.columns = "";
//        this.table = "";
//        this.parameters = "";
//        for (Enum e : enumerators) {
//            if(e instanceof Column) {
//                columns += ((Column) e).getColumn() + ", ";
//            } else if (e instanceof Table) {
//                table += ((Table) e).getTable() + " ";
//            } else if (e instanceof Param) {
//                parameters += ((Param) e).getParam();
//            } else if (e instanceof Operand) {
//                parameters += ((Operand) e).getOperand();
//            }
//        }
//        columns = columns.substring(0, columns.length() - 1) + " ";
//        
//        query = "SELECT " + columns + "FROM " + table + 
//    }
//
//    public String addColumns(String... columns) {
//        Iterator<String> it = Arrays.asList(columns).iterator();
//
//        while (it.hasNext()) {
//            query += it.next();
//            if (it.hasNext()) {
//                query += ", ";
//            } else {
//                query += " ";
//            }
//        }
//        return query;
//    }
//    
//    public String addTable(String table) {
//        query += "FROM" + table + " ";
//        return query;
//    }
//    
//    public String addjoins(String... table) {
//        Iterator<String> it = Arrays.asList(table).iterator();
//        
//        while (it.hasNext()) {
//            query += "LEFT JOIN " + it.next() + " ";
//        }
//        
//        return query;
//    }
//    
//    public String addParam(String param, Param operand) {
//        query += operand.getParam() + " " + param + " ";
//        return query;
//    }
//    
//    public String addParams(Map<String, Param> params) {
//        for (String s : params.keySet()) {
//            query += params.get(s) + " " + s + " ";
//        }
//        
//        return query;
//    }
//
//}
