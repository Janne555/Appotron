/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import java.util.List;
import sql.db.Database;
import storables.SessionControl;
import storables.User;

/**
 *
 * @author Janne
 */
public class SessionControlDao {
    private Database db;
    private UserDao uDao;

    public SessionControlDao(Database db) {
        this.db = db;
        this.uDao = new UserDao(db);
    }
    
    public void createSession(SessionControl session) throws SQLException {
        db.update("INSERT INTO Session(session_id, user_id, date) VALUES(?,?,?)", session.getObjs());
    }
    
    public User getUser(String sessionId) throws SQLException {
        List<String> list = db.queryAndCollect("SELECT * FROM Session, age(CURRENT_TIMESTAMP, session.date) WHERE age < interval '1 day' AND session_id = ?", rs ->{
            return rs.getString("user_id");
        }, sessionId);
        
        if (list.isEmpty()) return null;
        
        return uDao.findUserById(list.get(0));
    }
    
    public void deleteSession(String sessioncontrolid) throws SQLException {
        db.update("DELETE FROM Session WHERE session_id = ?", sessioncontrolid);
    }
}
