/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import util.Incrementer;
import util.Type;

/**
 *
 * @author Janne
 */
public class Item implements Comparable<Item>, SearchResult {

    private String uuid;
    private String name;
    private String serialNumber;
    private String location;
    private Timestamp createdOn;
    private Timestamp expiration;
    private List<Tag> tags;
    private Type type;

    public Item(String uuid,
            String name,
            String serialNumber,
            String location,
            Timestamp createdOn,
            Timestamp expiration,
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
        this.expiration = expiration;
        this.tags = tags;
        this.type = type;
    }

    public Item(String uuid,
            String name,
            String serialNumber,
            String location,
            Timestamp createdOn,
            Timestamp expiration,
            List<Tag> tags,
            Type type) {
        this(uuid, name, serialNumber, location, createdOn, expiration, null, tags, type);
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
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

    @Override
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

    @Override
    public Type getType() {
        return type;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getExpires() {
        if (expiration == null) {
            return "unknown";
        }

        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59);
        LocalDateTime tomorrow = endOfDay.plusDays(1);

        if (expiration.toLocalDateTime().isBefore(endOfDay)) {
            return "Today";
        } else if (expiration.toLocalDateTime().isAfter(endOfDay) && expiration.toLocalDateTime().isBefore(tomorrow)) {
            return "Tomorrow";
        } else if (expiration.toLocalDateTime().isBefore(LocalDateTime.now())) {
            return "EXPIRED";
        } else {
            long until = LocalDateTime.now().until(expiration.toLocalDateTime(), ChronoUnit.DAYS);
            return "In " + until + " days";
        }
    }

    public Object[] getObjs() {
        Object[] objs = new Object[8];
        Incrementer inc = new Incrementer();
        objs[inc.next()] = uuid;
        objs[inc.next()] = name;
        objs[inc.next()] = serialNumber;
        objs[inc.next()] = location;
        objs[inc.next()] = createdOn;
        objs[inc.next()] = expiration;
        objs[inc.next()] = type.getType();
        objs[inc.next()] = false;
        return objs;
    }

    @Override
    public String toString() {
        String result
                = "UUID: " + uuid + ", "
                + "NAME: " + name + ", "
                + "SERIAL: " + serialNumber + ", "
                + "Location: " + location + ", "
                + "EXPIRES: " + expiration + ", "
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
    
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if(!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;
        
        return this.uuid.equals(item.uuid)
                && this.name.equals(item.name)
                && this.serialNumber.equals(item.serialNumber)
                && this.location.equals(item.location);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.uuid);
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.serialNumber);
        hash = 67 * hash + Objects.hashCode(this.location);
        hash = 67 * hash + Objects.hashCode(this.createdOn);
        hash = 67 * hash + Objects.hashCode(this.expiration);
        return hash;
    }

    @Override
    public String getIdentifier() {
        return this.getSerialNumber();
    }

    @Override
    public Timestamp getDate() {
        return this.getCreatedOn();
    }
}
