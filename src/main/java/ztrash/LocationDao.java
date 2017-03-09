package ztrash;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package sql.db;
//
//import java.sql.SQLException;
//import java.util.List;
//import storables.Location;
//
//
//public class LocationDao {
//    private Database db;
//
//    public LocationDao(Database db) {
//        this.db = db;
//    }
//    
//    public Location create(Location t) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public Location findOne(String key) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public List<Location> findAll() throws SQLException {
//        List<Location> queryAndCollect = db.queryAndCollect("SELECT * FROM Location", (rs) -> {
//            return new Location(rs.getString("name"), rs.getInt("id"));
//        });
//        return queryAndCollect;
//    }
//
//    public void update(String key, Location t) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    public void delete(String key) throws SQLException {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    
//}
