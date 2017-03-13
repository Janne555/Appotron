/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.UUID;
import util.Incrementer;

/**
 *
 * @author Janne
 */
public class SessionControl {

    private String sessionId;
    private String userId;
    private Timestamp date;

    public SessionControl(String sessionId, String userId, Timestamp date) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.date = date;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void genNewSessionId() {
        setSessionId(UUID.randomUUID().toString());
    }

    public Object[] getObjs() {
        Object[] objs = new Object[3];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = sessionId;
        objs[inc.next()] = userId;
        objs[inc.next()] = date;
        return objs;
    }
}
