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
public enum Type {
    ITEM, FOODSTUFF, BOOK;
    
    public static Type parseType(String type) {
        switch (type) {
            case "item":
                return ITEM;
            case "book":
                return BOOK;
            case "foodstuff":
                return FOODSTUFF;
            default:
                throw new AssertionError();
        }
    }
}
