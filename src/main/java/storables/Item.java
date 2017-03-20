/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Janne
 */
public class Item implements Comparable<Item>, SearchResult {

    private int id;
    private String location;
    private ItemInfo itemInfo;
    private Timestamp date;
    private Timestamp expiration;
    private List<Tag> tags;

    public Item(int id,
            String location,
            Timestamp createdOn,
            Timestamp expiration,
            List<Tag> tags,
            ItemInfo itemInfo) {

        this.tags = tags;
        this.location = location;
        this.id = id;
        this.itemInfo = itemInfo;
        this.date = createdOn;
        this.expiration = expiration;
    }

    public String getIdentifier() {
        return itemInfo.getIdentifier();
    }

    public void setIdentifier(String identifier) {
        this.itemInfo.setIdentifier(identifier);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.itemInfo.getName();
    }

    public void setName(String name) {
        this.itemInfo.setName(name);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Timestamp date() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Tag> getSpecificTags() {
        return tags;
    }

    public List<Tag> getItemInfoTags() {
        return this.itemInfo.getTags();
    }

    public List<Tag> getTags() {
        List<Tag> alltags = tags;
        alltags.addAll(this.itemInfo.getTags());
        return alltags;
    }

    public void setSpecificTags(List<Tag> specificTags) {
        this.tags = specificTags;
    }

    public void setItemInfoTags(List<Tag> itemInfoTags) {
        this.itemInfo.setTags(itemInfoTags);
    }

    @Override
    public String getType() {
        return itemInfo.getType();
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public void setType(String type) {
        this.itemInfo.setType(type);
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

    @Override
    public String toString() {
        String result
                = "UUID: " + id + ", "
                + "NAME: " + itemInfo.getName() + ", "
                + "SERIAL: " + itemInfo.getIdentifier() + ", "
                + "Location: " + location + ", "
                + "EXPIRES: " + expiration + ", "
                + "CREATED: " + date + ", ";

        for (Tag tag : getTags()) {
            result += ", " + tag;
        }
        return result;
    }

    @Override
    public int compareTo(Item t) {
        return getName().compareTo(t.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        Item item = (Item) o;

        return this.id == item.id
                && getName().equals(item.getName())
                && getIdentifier().equals(item.getIdentifier())
                && this.location.equals(item.location);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.itemInfo.getName());
        hash = 67 * hash + Objects.hashCode(this.itemInfo.getIdentifier());
        hash = 67 * hash + Objects.hashCode(this.location);
        hash = 67 * hash + Objects.hashCode(this.date);
        hash = 67 * hash + Objects.hashCode(this.expiration);
        return hash;
    }

    public Timestamp getDate() {
        return date;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }
}
