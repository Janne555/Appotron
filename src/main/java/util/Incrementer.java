/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Janne
 */
public class Incrementer {
    private int number;

    public Incrementer() {
        number = -1;
    }
    
    public int next() {
        number++;
        return number;
    }
}