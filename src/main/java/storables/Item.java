/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private List<String> descriptions;
    private Map<String, String> tags;

    public Item(String uuid,
            String name,
            String serialNumber,
            String location,
            int locationId,
            Timestamp createdOn,
            List<String> descriptions,
            Map<String, String> tags) {

        this.uuid = uuid;
        this.name = name;
        this.serialNumber = serialNumber;
        this.location = location;
        this.locationId = locationId;
        this.createdOn = createdOn;
        this.descriptions = descriptions;
        this.tags = tags;
    }

    public Item(String name,
            String serial_number,
            String location,
            int locationId,
            Type type,
            List<String> descriptions,
            Map<String, String> tags) {
        this(UUID.randomUUID().toString(), name, serial_number, location, locationId, new Timestamp(System.currentTimeMillis()), descriptions, tags);
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
    
    public List<String> getDescriptions() {
        return descriptions;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public int getLocationId() {
        return locationId;
    }
    
    @Override
    public String toString() {
        return uuid + ", "
                + name + ", "
                + serialNumber + ", "
                + location + ", "
                + createdOn + ", "
                + descriptions + ", "
                + tags;
    }
}
