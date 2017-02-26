/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.List;
import sql.db.Type;

/**
 *
 * @author Janne
 */
public class Item {

    private String uuid;
    private String name;
    private String serialNumber;
    private String location;
    private int locationId;
    private Timestamp createdOn;
    private List<Tag> tags;
    private Type type;

    public Item(String uuid,
            String name,
            String serialNumber,
            String location,
            int locationId,
            Timestamp createdOn,
            List<Tag> uuidTags,
            List<Tag> tags,
            Type type) {
        
        tags.addAll(uuidTags);
        this.uuid = uuid;
        this.name = name;
        this.serialNumber = serialNumber;
        this.location = location;
        this.locationId = locationId;
        this.createdOn = createdOn;
        this.tags = tags;
        this.type = type;
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
        return location;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }
    
    public List<Tag> getTags() {
        return tags;
    }

    public int getLocationId() {
        return locationId;
    }
    
    public Object[] getObjs() {
        Object[] objs = new Object[7];
        objs[0] = uuid;
        objs[1] = name;
        objs[2] = serialNumber;
        objs[3] = locationId;
        objs[4] = createdOn;
        objs[5] = type.getType();
        objs[6] = "false";
        return objs;
    }
    
    @Override
    public String toString() {
        String result = uuid + ", "
                + name + ", "
                + serialNumber + ", "
                + location + ", "
                + createdOn + ", ";
        
        for (Tag tag : tags) {
            result += ", " + tag;
        }
        return result;
    }
}
