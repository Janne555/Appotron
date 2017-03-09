/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.NutritionalInfo;

public class NutritionalInfoDao {

    private Database db;

    public NutritionalInfoDao(Database db) {
        this.db = db;
    }

    public NutritionalInfo findOne(String identifier) throws SQLException {
        List<NutritionalInfo> queryAndCollect = db.queryAndCollect("SELECT * FROM NutritionalInfo WHERE identifier = ?", rs -> {
            return new NutritionalInfo(0, identifier, rs.getFloat("energy"), rs.getFloat("carbohydrate"), rs.getFloat("fat"), rs.getFloat("protein"));
        }, identifier);
        
        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }
    
    public void create(NutritionalInfo nuInfo) throws SQLException {
        db.update("INSERT INTO NutritionalInfo(identifier, energy, carbohydrate, fat, protein) VALUES(?,?,?,?,?)", nuInfo.getObjs());
    }
}
