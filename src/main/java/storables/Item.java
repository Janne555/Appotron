/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.datetime.joda.LocalDateParser;
import util.Incrementer;
import util.Type;

/**
 *
 * @author Janne
 */
public class Item implements Comparable<Item> {

    private String uuid;
    private String name;
    private String serialNumber;
    private String location;
    private Timestamp createdOn;
    private List<Tag> tags;
    private Type type;

    public Item(String uuid,
            String name,
            String serialNumber,
            String location,
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
            String location,
            Timestamp createdOn,
            List<Tag> tags,
            Type type) {
        this(uuid, name, serialNumber, location, createdOn, null, tags, type);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public long getDaysUntilExpiration() {
        for (Tag tag : this.tags) {
            if (tag.getKey().equals("expiration")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime time = LocalDateTime.parse(tag.getValue(), formatter);
                LocalDateTime now = LocalDateTime.now();
                return now.until(time, ChronoUnit.DAYS);
                
            }
        }
        return 0;
    }


    public Object[] getObjs() {
        Object[] objs = new Object[7];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = uuid;
        objs[inc.next()] = name;
        objs[inc.next()] = serialNumber;
        objs[inc.next()] = location;
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
                + "Location: " + location + ", "
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
