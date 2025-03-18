package org.structures.array.searchs;

/**
 *
 * @author martin
 */
public class TSearcher<T> extends Thread{
    private T[] array;
    private T objReference;
    private int indexFind;
    private int from;
    private int to;

    public TSearcher(T[] array, T objReference, int from, int to) {
        this.array = array;
        this.objReference = objReference;
        this.from = from;
        this.to = to;
        this.indexFind = -2;
    }

    public boolean hasResults(){
        return indexFind >= -1;
    }

    public int getIndexFind() {
        return indexFind;
    }

    @Override
    public void run() {
        for (int i = from; i < to; i++)
            if (array[i].equals(objReference)) {
                indexFind = i;
                break;
            }
        if (indexFind == -2)
            indexFind = -1;
        objReference = null;
        array = null;
    }

}
