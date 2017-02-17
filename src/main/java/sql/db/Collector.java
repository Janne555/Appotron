/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sql.db;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Janne
 */
public interface Collector<T> {
    T collect(ResultSet rs) throws SQLException;
}
