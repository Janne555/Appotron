package ztrash;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package storables;
//
///**
// *
// * @author Janne
// */
//public class Location implements Comparable<Location> {
//    private String name;
//    private int id;
//
//    public Location(String name, int id) {
//        this.name = name;
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    @Override
//    public int compareTo(Location t) {
//        return name.compareTo(t.getName());
//    }
//    
//    public String getString() {
//        return "" + this.id + ":" + this.name;
//    }
//    
//    public static Location parseLocation(String locationString) {
//        int colon = locationString.indexOf(":");
//        
//        int intPart = Integer.parseInt(locationString.substring(0, colon));
//        String namePart = locationString.substring(colon + 1);
//        
//        return new Location(namePart, intPart);
//    }
//    
//}
