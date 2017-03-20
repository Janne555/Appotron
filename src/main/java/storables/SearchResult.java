/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storables;

import java.sql.Timestamp;
import util.Type;

/**
 *
 * @author Janne
 */
public interface SearchResult {
    public String getName();
    public String getLocation();
    public String getType();
    public Timestamp getDate();
}
