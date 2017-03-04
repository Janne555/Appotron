/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import sql.db.Incrementer;
import sql.db.Type;

/**
 *
 * @author Janne
 */
public class Item implements Comparable<Item> {

    private String uuid;
    private String name;
    private String serialNumber;
    private Location location;
    private Timestamp createdOn;
    private List<Tag> tags;
    private Type type;

    public Item(String uuid,
            String name,
            String serialNumber,
            Location location,
            Timestamp createdOn,
            List<Tag> uuidTags,
            List<Tag> tags,
            Type type) {
        
        if (tags == null) {
            tags = new ArrayList<>();
        } else if (uuidTags != null) {
            tags.addAll(uuidTags);
        }
        
        this.uuid = uuid;
        this.name = name;
        this.serialNumber = serialNumber;
        this.location = location;
        this.createdOn = createdOn;
        this.tags = tags;
        this.type = type;
    }
    
    public Item(String uuid,
            String name,
            String serialNumber,
            String locationName,
            int locationId,
            Timestamp createdOn,
            List<Tag> uuidTags,
            List<Tag> tags,
            Type type) {
        this(uuid, name, serialNumber, new Location(locationName, locationId), createdOn, uuidTags, tags, type);
    }
    
    public Item(String uuid,
            String name,
            String serialNumber,
            Location location,
            Timestamp createdOn,
            List<Tag> tags,
            Type type) {
        this(uuid, name, serialNumber, location, createdOn, null, tags, type);
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getLocation() {
        return location.getName();
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public int getLocationId() {
        return location.getId();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Object[] getObjs() {
        Object[] objs = new Object[7];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = uuid;
        objs[inc.next()] = name;
        objs[inc.next()] = serialNumber;
        objs[inc.next()] = getLocationId();
        objs[inc.next()] = createdOn.toString();
        objs[inc.next()] = type.getType();
        objs[inc.next()] = "false";
        return objs;
    }

    @Override
    public String toString() {
        String result = 
                "UUID: " + uuid + ", "
                + "NAME: " + name + ", "
                + "SERIAL: " + serialNumber + ", "
                + "Location: " + location.getString() + ", "
                + "CREATED: " + createdOn + ", ";

        for (Tag tag : tags) {
            result += ", " + tag;
        }
        return result;
    }

    @Override
    public int compareTo(Item t) {
        return name.compareTo(t.getName());
    }
}
