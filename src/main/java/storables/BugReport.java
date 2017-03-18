/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import util.Incrementer;

/**
 *
 * @author Janne
 */
public class BugReport {
    private String userId;
    private User user;
    private String subject;
    private String description;
    private Timestamp date;

    public BugReport(String userId, User user, String subject, String description, Timestamp date) {
        this.userId = userId;
        this.user = user;
        this.subject = subject;
        this.description = description;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Object[] getObjs() {
        Object[] objs = new Object[4];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = subject;
        objs[inc.next()] = description;
        objs[inc.next()] = userId;
        objs[inc.next()] = date;
        return objs;
    }
}
