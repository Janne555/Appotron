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
        db.update("INSERT INTO SessionControl(id, users_id, date) VALUES(?,?,?)",
                session.getSessionId(), session.getUserId(), session.getDate());
    }
    
    public SessionControl findValidSessionById(String sessionId) throws SQLException {
        List<SessionControl> list = db.queryAndCollect("SELECT * FROM SessionControl, age(CURRENT_TIMESTAMP, SessionControl.date) WHERE age < interval '1 day' AND id = ?", rs ->{
            return new SessionControl(rs.getString("id"), rs.getString("users_id"), rs.getTimestamp("date"));
        }, sessionId);
        
        if (list.isEmpty()) return null;
        
        return list.get(0);
    }
    
    public User getValidUser(String sessionId) throws SQLException {
        SessionControl session = findValidSessionById(sessionId);
        if (session == null) return null;
        String userId = session.getUserId();
        User findById = uDao.findById(userId);
        return findById;
    }
    
    public boolean invalidateSession(String sessionId) throws SQLException {
        db.update("DELETE FROM SessionControl WHERE id = ?", sessionId);
        return true;
    }
    
}
