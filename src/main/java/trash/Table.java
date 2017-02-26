///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.db;
//
///**
// *
// * @author Janne
// */
//public enum Table {
//    ITEM("Item"),
//    LOC("Location"),
//    TAG("Tag"),
//    DESCR("Description");
//    
//
//    private final String table;
//
//    Table(String table) {
//        this.table = table;
//    }
//
//    public String getTable() {
//        return table;
//    }
//
//    public static Table parseSearch(String search) {
//        switch (search) {
//            case "uuid":
//                return UUID;
//            case "name":
//                return NAME;
//            case "serial":
//                return SERIAL;
//            case "location_name":
//                return LOC_NAME;
//            case "location_id":
//                return LOC_ID;
//            case "created_on":
//                return CREATED_ON;
//            case "type":
//                return TYPE;
//            default:
//                throw new AssertionError();
//        }
//    }
//}
