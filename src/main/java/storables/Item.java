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

/**
 *
 * @author Janne
 */
public class Item {

    private String uuid;
    private String name;
    private String serial_number;
    private String location;
    private int locationId;
    private Timestamp created_on;
    private List<String> descriptions;
    private Map<String, String> tags;

    public Item(String uuid,
            String name,
            String serial_number,
            String location,
            int locationId,
            Timestamp created_on,
            List<String> descriptions,
            Map<String, String> tags) {

        this.uuid = uuid;
        this.name = name;
        this.serial_number = serial_number;
        this.location = location;
        this.locationId = locationId;
        this.created_on = created_on;
        this.descriptions = descriptions;
        this.tags = tags;
    }

    public Item(String name,
            String serial_number,
            String location,
            int locationId,
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

    public String getSerial_number() {
        return serial_number;
    }

    public String getLocation() {
        return location;
    }

    public Timestamp getCreated_on() {
        return created_on;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public void setDescriptions(List<String> descriptions) {
        this.descriptions = descriptions;
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
                + serial_number + ", "
                + location + ", "
                + ", "
                + descriptions + ", "
                + tags;
    }
}
