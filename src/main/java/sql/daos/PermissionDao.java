/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.daos;

import java.sql.SQLException;
import sql.db.Database;
import storables.Permission;

/**
 *
 * @author Janne
 */
public class PermissionDao {
    private Database db;

    public PermissionDao(Database db) {
        this.db = db;
    }
    
    public Permission store(Permission permission) throws SQLException {
        int update = db.update("INSERT INTO permission(person_identifier, item_id, canedit, candelete) VALUES(?,?,?,?)", true,
                permission.getPersonIdentifier(),
                permission.getItemId(),
                permission.canEdit(),
                permission.canDelete());
        permission.setId(update);
        return permission;
    }
}
