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
//public enum Column {
//    UUID("Item.uuid"),
//    NAME("Item.name"),
//    SERIAL("Item.serial_number"),
//    LOC_NAME("Location.name AS location_name"),
//    LOC_ID("Location.id AS location_id"),
//    CREATED_ON("Item.created_on"),
//    TYPE("Item.type"),
//    ITEM_UUID("item_uuid");
//
//    private final String column;
//
//    Column(String column) {
//        this.column = column;
//    }
//
//    public String getColumn() {
//        return column;
//    }
//
//    public static Column parseSearch(String search) {
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
