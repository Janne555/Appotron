/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import com.google.gson.JsonObject;
import java.util.List;
import sql.db.Type;

/**
 *
 * @author Janne
 */
public class Tag {

    private int id;
    private String identifier;
    private String key;
    private String value;
    private String type;

    public Tag(int id, String identifier, String key, String value, String type) {
        this.identifier = identifier;
        this.key = key;
        this.value = value;
        this.id = id;
        this.type = type;
    }

    public Tag(int id, String identifier, String key, String value, Type type) {
        this.identifier = identifier;
        this.key = key;
        this.value = value;
        this.id = id;
        this.type = type.getType();
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Object[] getObjs() {
        Object[] objs = new Object[4];
        objs[0] = identifier;
        objs[1] = key;
        objs[2] = value;
        objs[3] = type;
        return objs;
    }

    public Object[] getObjsId() {
        Object[] objs = new Object[5];
        objs[0] = identifier;
        objs[1] = key;
        objs[2] = value;
        objs[3] = type;
        objs[4] = id;
        return objs;
    }

    @Override
    public String toString() {
        return key + "=" + value;
    }

    public static JsonObject getJsonObject(List<Tag> tags) {
        JsonObject json = new JsonObject();
        tags.forEach((tag) -> {
            json.addProperty(tag.getKey(), tag.getValue());
        });
        return json;
    }

}
