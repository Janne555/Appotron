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
public class Foodstuff {

    private String uuid;
    private String name;
    private String serial_number;
    private String location;
    private Calendar expiration;
    private Calendar created_on;
    private List<String> descriptions;
    private Map<String, String> tags;
}
