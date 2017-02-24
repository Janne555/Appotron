/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Janne
 */
public class Foodstuff extends Item {
    private Timestamp expirationDate;

    public Foodstuff(String uuid, 
            String name,
            String serial_number,
            String location,
            int locationId,
            Timestamp created_on, 
            List<String> descriptions,
            Map<String, String> tags,
            Timestamp expirationDate) {
        super(uuid, name, serial_number, location, locationId, created_on, descriptions, tags);
        this.expirationDate = expirationDate;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }
    
    @Override
    public String toString() {
        return super.toString() + ", " + expirationDate;
    }
}
