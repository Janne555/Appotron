/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import sql.db.Database;
import storables.BugReport;

/**
 *
 * @author Janne
 */
public class BugReportDao {
    private Database db;

    public BugReportDao(Database db) {
        this.db = db;
    }
    
    public void create(BugReport bugReport) throws SQLException {
        db.update("INSERT INTO bugreport(subject, description, users_id, date) VALUES(?, ?, ?, ?)", bugReport.getObjs());
    }
}
