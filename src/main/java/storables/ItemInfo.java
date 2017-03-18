/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.util.List;

/**
 *
 * @author Janne
 */
public class ItemInfo {
    private int id;
    private String name;
    private String type;
    private String identifier;
    private List<Tag> tags;

    public ItemInfo(int id, String name, String type, String identifier, List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.identifier = identifier;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
