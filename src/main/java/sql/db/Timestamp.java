/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Janne
 */
public class Timestamp extends java.sql.Timestamp {
    public Timestamp(String timestampString) {
        super(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse(timestampString, formatter);
        super.setTime(time.toEpochSecond(ZoneOffset.UTC) * 1000);
    }
}
