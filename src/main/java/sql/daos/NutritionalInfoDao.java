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
    private ItemInfoDao infoDao;

    public NutritionalInfoDao(Database db) {
        this.db = db;
        this.infoDao = new ItemInfoDao(db);
    }

    public NutritionalInfo findOneByItemInfoId(int itemInfoId) throws SQLException {
        List<NutritionalInfo> queryAndCollect = db.queryAndCollect("SELECT * FROM NutritionalInfo WHERE iteminfo_id = ?", rs -> {
            return new NutritionalInfo(infoDao.findOne(rs.getInt("iteminfo_id")), rs.getFloat("energy"), rs.getFloat("carbohydrate"), rs.getFloat("fat"), rs.getFloat("protein"));
        }, itemInfoId);
        
        if (queryAndCollect.isEmpty()) {
            return null;
        } else {
            return queryAndCollect.get(0);
        }
    }
    
    public void store(NutritionalInfo nuInfo) throws SQLException {
        db.update("INSERT INTO NutritionalInfo(iteminfo_id, energy, carbohydrate, fat, protein) VALUES(?,?,?,?,?)", false,
                nuInfo.getItemInfoId(),
                nuInfo.getEnergy(),
                nuInfo.getCarbohydrates(),
                nuInfo.getFat(),
                nuInfo.getProtein());
    }
}
