/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Janne
 */
public enum Type {
    ITEM("item"),
    FOODSTUFF("foodstuff"),
    BOOK("book"),
    SERIAL("serial"),
    UUID("uuid"),
    INGREDIENT("ingredient"),
    SERVING("serving"),
    SESSION("session"),
    MEAL("meal");

    private final String type;

    private Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Type getType(String type) {
        for (Type c : values()) {
            if (c.getType().equals(type)) {
                return c;
            }
        }
        throw new IllegalArgumentException(type);
    }
}
