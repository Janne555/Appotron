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
    ITEM("item"), 
    FOODSTUFF("foodstuff"),
    BOOK("book");
    
    private final String type;

    private Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    
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
