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
    
    public void store(SessionControl session) throws SQLException {
        db.update("INSERT INTO SessionControl(sessionid, person_identifier, date) VALUES(?,?,?)", false,
                session.getSessionId(), session.getUserId(), session.getDate());
    }
    
    public SessionControl findValidSessionById(String sessionId) throws SQLException {
        List<SessionControl> list = db.queryAndCollect("SELECT * FROM SessionControl WHERE CURRENT_TIMESTAMP < SessionControl.date AND sessionid = ?", rs ->{
            return new SessionControl(rs.getString("sessionid"), rs.getString("person_identifier"), rs.getTimestamp("date"));
        }, sessionId);
        
        if (list.isEmpty()) return null;
        
        return list.get(0);
    }
    
    public User getValidUser(String sessionId) throws SQLException {
        SessionControl session = findValidSessionById(sessionId);
        if (session == null) return null;
        String userId = session.getUserId();
        User findById = uDao.findById(userId);
        refreshSession(sessionId);
        return findById;
    }
    
    public boolean refreshSession(String sessionId) throws SQLException {
        if (findValidSessionById(sessionId) != null) {
            db.update("UPDATE sessioncontrol SET date = CURRENT_TIMESTAMP + interval '6 hour' WHERE sessionid = ?", false, sessionId);
            return true;
        }
        
        return false;
    }
    
    public boolean invalidateSession(String sessionId) throws SQLException {
        db.update("DELETE FROM SessionControl WHERE sessionid = ?", false, sessionId);
        return true;
    }
    
}
