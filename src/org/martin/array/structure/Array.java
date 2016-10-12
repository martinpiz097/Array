
package org.martin.array.structure;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Lista que controla de manera dinámica un Array genérico.
 * @author martin
 * @param <E> Tipo de elementos que tendrá la lista.
 */
public class Array<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable{
    private E[] array;
    private int size;
    private int capacity;
    private static final int defaultCapacity = 100;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE-8;
   
    /**
     * Constructor por defecto, instancia la lista con una capacidad de 100
     * elementos (capacidad por defecto).
     */
    
    public Array() {
        this(defaultCapacity);
    }

    /**
     * Constructor que recibe la capacidad mínima que la lista
     * tendrá al comienzo. Si ésta es menor a la capacidad por defecto
     * ésta última será la inicial.
     * @param initialCapacity Capacidad inicial a establecer.
     */
    public Array(int initialCapacity) {
        if (initialCapacity < defaultCapacity)
            array = (E[]) new Object[defaultCapacity];
        else
            array = (E[]) new Object[initialCapacity];
        capacity = array.length;
    }
    
    /**
     * Constructor que recibe una lista con los datos iniciales.
     * @param collection Lista que se cargará al arreglo dinámico.
     */
    public Array(Collection<? extends E> collection){
        this(collection.size());
        addAll(collection);
    }
    
    private E[] createArray(int size){
        return (E[]) new Object[size];
    }
    
    private void grow(int minCapacity){
        int oldCapacity = array.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        array = Arrays.copyOf(array, newCapacity);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        capacity++;
         
        if (minCapacity - array.length > 0)
            grow(minCapacity);
    }
    
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
            Integer.MAX_VALUE :
            MAX_ARRAY_SIZE;
    }
    
    private void goBack(int fromIndex){
        for (int i = fromIndex; i < size; i++) {
            if (i == 0) {
                array[i] = array[i+1];
                //first = array[i];
            }
            else if (size == i-1) {
                array[i] = null;
                //last = array[i-1];
            }
            else
                array[i] = array[i+1];
        }
        size--;
    }
    
    private void openSpace(int index, int spacesNumber){
        checkCapacity();
        
        if (index+1 == size)
            array[index+spacesNumber] = array[index];
        
        else{
            E current;
            E nearby = null;
            
            for (int i = index; i < size; i++) {
                if (i == index) {
                    nearby = array[i+spacesNumber];
                    array[i+spacesNumber] = array[i];
                }
                else {
                    current = nearby;
                    nearby = array[i + spacesNumber];
                    array[i + spacesNumber] = current;
                }
            }
            size+=spacesNumber;
        }
    }
    
    private void openSpaceAt(int index){
        checkCapacity();
        
        if (index+1 == size)
            array[index+1] = array[index];
        
        else{
            E current;
            E next = null;
            for (int i = index; i < size; i++) {
                if (i == index) {
                    next = array[i+1];
                    array[i+1] = array[i];
                }
                else {
                    current = next;
                    next = array[i + 1];
                    array[i + 1] = current;
                }
            }
        }
        size++;
    }
    
    private void checkIndex(int index){
        if(index >= size || index < 0)
            throw new IndexOutOfBoundsException(index+"");
    }

    private void checkCapacity(){
        if (size == array.length)
            grow(size+1);
    }

    private void checkMemory(){
        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();
        
        if (maxMemory - freeMem <= 80000000)
            System.gc();
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        synchronized(array){
            for (int i = 0; i < size; i++) 
            if (array[i].equals(o))
                return true;
            return false;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>();
    }

    @Override
    public Object[] toArray() {
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return (T[]) array;
    }

    @Override
    public boolean add(E e) {
        synchronized(array){
            ensureExplicitCapacity(size+1);
            array[size] = e;
            size++;
            checkMemory();
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean deleted = false;
        synchronized (array) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(o)) {
                    goBack(i);
                    deleted = true;
                    break;
                }
            }
        }
        return deleted;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(c.isEmpty()) return false;
        synchronized(array){
            boolean containsAll = false;
            for (Object object : c) {
                for (int i = 0; i < size; i++) {
                    if (array.equals(object)) {
                        containsAll = true;
                        break;
                    }
                    else
                        containsAll = false;
                }
            }
            
            return containsAll;
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        synchronized(array){
            for (E e : c) {
                ensureExplicitCapacity(size+1);
                array[size] = e;
                size++;
            }
            checkMemory();
            return true;
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        synchronized(array){
            checkIndex(index);
            
            if(!isEmpty() && !c.isEmpty()) {
//                openSpace(index, c.size());
//                for (E e : c) {
//                    array[size] = e;
//                    size++;
//                }
                List<E> subListStart = subList(0, index);
                List<E> subListFinish = subList(index, size);
                clear();
                addAll(subListStart);
                addAll(c);
                addAll(subListFinish);
                checkMemory();
                return true;
            }
            else
                return false;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        synchronized(array){
            boolean deleted = false;
            for (int i = 0; i < size; i++) {
                for (Object object : c) {
                    if (array[i].equals(object)) {
                        goBack(i);
                        deleted = true;
                        break;
                    }
                    else
                        deleted = false;
                }
            }
            return deleted;
        }
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        if (c.isEmpty())
            return false;
        
        clear();
        for (Object object : c)
            add((E) object);
        return true;
    }

    @Override
    public void clear() {
        //first = null;
        //last = null;
        synchronized (array) {
            for (int i = 0; i < size; i++)
                array[i] = null;
            size = 0;
        }
    }

    public E getFirst(){
        checkIndex(0);
        return array[0];
    }
    
    public E getLast(){
        checkIndex(size-1);
        return array[size-1];
    }
    
    @Override
    public E get(int index) {
        checkIndex(index);
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        synchronized(array){
            checkIndex(index);
            E e = get(index);
            array[index] = element;
            return e;
        }
    }

    @Override
    public void add(int index, E element) {
        checkIndex(index);
        synchronized(array){
            if(!isEmpty()) openSpaceAt(index);
            array[index] = element;
        }
    }

    @Override
    public E remove(int index) {
        synchronized(array){
            checkIndex(index);
            E e = array[index];
            goBack(index);
            return e;
        }
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++)
            if (array[i].equals(o))
                return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex-1);
        List<E> list = new Array<>();
        
        for (int i = fromIndex; i < toIndex; i++)
            list.add(array[i]);
        return list;
    }

    private class ArrayIterator<E> implements Iterator<E>{
        private int index; 

        public ArrayIterator() {
            index = -1;
        }
        
        @Override
        public boolean hasNext() {
            return index+1 < size;
        }

        @Override
        public E next() {
            index++;
            return (E) array[index];
        }
        
    }
}
