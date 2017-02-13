/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Janne
 */
public class Book {

    private String uuid;
    private String title;
    private String author;
    private String publisher;
    private int publishing_year;
    private String isbn;
    private String location;
    private Calendar created_on;
    private List<String> descriptions;
    private Map<String, String> tags;
}
