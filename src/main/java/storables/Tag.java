/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import com.google.gson.JsonObject;
import java.util.List;

/**
 *
 * @author Janne
 */
public class Tag {
    private int id;
    private int itemId;
    private String key;
    private String value;

    public Tag(int id, int itemId, String key, String value) {
        this.id = id;
        this.itemId = itemId;
        this.key = key;
        this.value = value;
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

    @Override
    public String toString() {
        return key + "=" + value;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public static JsonObject getJsonObject(List<Tag> tags) {
        JsonObject json = new JsonObject();
        tags.forEach((tag) -> {
            json.addProperty(tag.getKey(), tag.getValue());
        });
        return json;
    }

}
