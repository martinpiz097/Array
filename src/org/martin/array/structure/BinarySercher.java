/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.array.structure;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author martin
 */
public class BinarySercher {
    public static <T> int search(T[] array, T refObject){
        int lim = array.length;
        int counter = lim-1;
        int midle = lim/2;
        for (int i = 0; i < lim; i++) {
            if (array[i].equals(refObject)) {
                break;
            }
            else if (array[midle+i].equals(refObject)) {
                break;
            }
            else if (array[midle-i].equals(refObject)) {
                break;
            }
            else if (array[counter].equals(refObject)) {
                break;
            }
            counter--;
        }
        return -1;
    }
    
      public static <T> int search(T[] array, T refObject, int limit){
        int lim = limit;
        int counter = lim-1;
        int midle = lim/2;
        for (int i = 0; i < lim; i++) {
            if (array[i].equals(refObject)) {
                break;
            }
            else if (array[midle+i].equals(refObject)) {
                break;
            }
            else if (array[midle-i].equals(refObject)) {
                break;
            }
            else if (array[counter].equals(refObject)) {
                break;
            }
            counter--;
        }
        return -1;
    }
    
}
