/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

/**
 *
 * @author Janne
 */
public enum Search {
    UUID("Item.uuid = ?"),
    NAME("Item.name = ?"),
    SERIAL("Item.serial_number = ?"),
    LOCATION_NAME("location_name = ?"),
    LOCATION_ID("location_id = ?"),
    CREATED_ON("Item.created_on = ?"),
    CREATED_BEFORE("Item.created_on < ?"),
    CREATED_AFTER("Item.created_on > ?"),
    TYPE("Item.type = ?"),
    EXPIRES_BEFORE(""),
    EXPIRES_AFTER("");

    private final String term;

    Search(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public static Search parseSearch(String search) {
        switch (search) {
            case "uuid":
                return UUID;
            case "name":
                return NAME;
            case "serial":
                return SERIAL;
            case "location_name":
                return LOCATION_NAME;
            case "location_id":
                return LOCATION_ID;
            case "created_on":
                return CREATED_ON;
            case "type":
                return TYPE;
            case "created_before":
                return CREATED_BEFORE;
            case "created_after":
                return CREATED_AFTER;
            case "expires_before":
                return EXPIRES_BEFORE;
            case "expires_after":
                return EXPIRES_AFTER;
            default:
                throw new AssertionError();
        }
    }
}
