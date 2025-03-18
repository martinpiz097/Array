package org.structures.array.searchs;

/**
 *
 * @author martin
 */
public class BinarySearcher {

    public static <T> int search(T[] array, T refObject) {
        int lim = array.length;
        int counter = lim - 1;
        int midle = lim / 2;
        for (int i = 0; i < midle + 1; i++) {
            if (array[i].equals(refObject)) {
                return i;
            } else if (array[midle + i].equals(refObject)) {
                return i;
            } else if (array[midle - i].equals(refObject)) {
                return i;
            } else if (array[counter].equals(refObject)) {
                return i;
            }
            counter--;
        }
        return -1;
    }

    public static <T> int search(T[] array, T refObject, int limit) {
        if (limit == 1)
            return array[0].equals(refObject) ? 0 : -1;

        int counter = limit - 1;
        int midle = limit / 2;

        if (array[0].equals(refObject))
            return 0;

        else if (array[midle].equals(refObject))
            return midle;

        else if (array[counter].equals(refObject))
            return counter;

        else {
            counter--;
            for (int i = 1; i < midle; i++) {
                if (array[i].equals(refObject)) {
                    return i;
                } else if (array[midle + i].equals(refObject)) {
                    return midle + i;
                } else if (array[midle - i].equals(refObject)) {
                    return midle - i;
                } else if (array[counter].equals(refObject)) {
                    return counter;
                } else if (array[counter - i].equals(refObject)) {
                    return counter - i;
                }
                counter--;
            }
            return -1;
        }
    }

}
