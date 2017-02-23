/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Janne
 */
public class Book extends Item {
    private String author;
    private String publisher;
    private int publishingYear;

    public Book(String uuid,
            String name,
            String serial_number,
            String location,
            int locationId,
            Timestamp created_on,
            List<String> descriptions,
            Map<String, String> tags) {
        super(uuid, name, serial_number, location, locationId, created_on, descriptions, tags);
        this.author = tags.remove("author");
        this.publisher = tags.remove("publisher");
        this.publishingYear = Integer.parseInt(tags.remove("publishing_year"));

    }

}
