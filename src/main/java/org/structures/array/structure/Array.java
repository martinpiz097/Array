
package org.structures.array.structure;

import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.function.Predicate;

/**
 * Lista que controla de manera dinámica un Array genérico.
 * @author martin
 * @param <E> Tipo de elementos que tendrá la lista.
 */
public class Array<E> extends AbstractList<E>
        implements Queue<E>, RandomAccess, Cloneable, Serializable {

    private transient E[] array;
    private transient int size;
    private transient int capacity;
    public static final int DEFAULT_CAPACITY = 100;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE-8;

    public static final <T> Array<T> newInstance(){
        return new Array<>();
    }

    /**
     * Constructor por defecto, instancia la lista con una capacidad de 100
     * elementos (capacidad por defecto).
     */

    public Array() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor que recibe la capacidad mínima que la lista
     * tendrá al comienzo. Si ésta es menor a la capacidad por defecto
     * ésta última será la inicial.
     * @param initialCapacity Capacidad inicial a establecer.
     */
    public Array(int initialCapacity) {
        if (initialCapacity < DEFAULT_CAPACITY)
            array = (E[]) new Object[DEFAULT_CAPACITY];
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

    public Array(E[] arrayData){
        array = arrayData;
        size = capacity = array.length;
    }

    private void updateCapacity(){
        capacity = array.length;
    }

    private E[] createArray(int size){
        return (E[]) new Object[size];
    }

    private void grow(int minCapacity){
        int newCapacity = capacity + (capacity >> 1);
//        if (newCapacity - minCapacity < 0)
//            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = Integer.MAX_VALUE;
        array = Arrays.copyOf(array, newCapacity);
        capacity = newCapacity;
    }

    // minCapacity --> la capacidad minima que debe contener
    // la lista, ya que, debe ser por lo menos size+1 para tener
    // espacios para más elementos.
    // capacity --> capacidad del arreglo(lenght).
    private void ensureCapacity(int minCapacity) {
        // si el size+1 es mayor que la capacidad del arreglo
        // si debe agrandar el array.
        if (minCapacity > capacity)
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
//        for (int i = fromIndex; i < size; i++) {
//            array[i] = array[i+1];
//                //first = array[i];
//        }
//
//        size--;

        // Rescato la cantidad de espacios a recorrer
        // al copiar el array.
        int numMoved = size - fromIndex - 1;
        if (numMoved > 0)
            // Copio desde el indice +1 desde el elemento a borrar al mismo array
            // partiendo desde el indice a borrar, por lo tanto el elemento sobrante
            // se borrara mas adelante.
            System.arraycopy(array, fromIndex+1, array, fromIndex, numMoved);
        array[--size] = null; // clear to let GC do its work
    }
//
//    private void reduct(int spacesCount){
//        for (int i = 0; i < size; i++) {
//
//        }
//    }

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
            try {
                array[index+1] = array[index];
            } catch (IndexOutOfBoundsException e) {
                grow(size+1);
                array[index+1] = array[index];
            }

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
        if (size+1 > capacity)
            grow(size+1);
    }

    private void checkMemory(){
        long freeMem = Runtime.getRuntime().freeMemory();
        long maxMemory = Runtime.getRuntime().maxMemory();

        if (maxMemory - freeMem <= 80000000)
            System.gc();
    }

    public E[] combine(E[] a1, E[] a2){
        if (a1 == null)
            return a2;
        else if (a2 == null)
            return a1;

        {
            int a;
            {

            }
        }

        final int lenA1 = a1.length;
        final int lenA2 = a2.length;
        final int newLenght = lenA1+lenA2;

        E[] newArray = (E[]) new Object[newLenght];

        int checkPoint = lenA1;

        System.arraycopy(a1, 0, newArray, 0, lenA1);

        for (int i = 0; i < lenA2; i++) {
            newArray[checkPoint] = a2[i];
            checkPoint++;
        }
        return newArray;
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
        for (int i = 0; i < size; i++)
            if (array[i].equals(o))
                return true;
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>();
    }

    @Override
    public E[] toArray() {
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if(a.length < size)
            a = (T[]) array;
        else
            System.arraycopy(array, 0, a, 0, size);
        return a;
    }

    public void copyTo(Object[] a){
        System.arraycopy(array, 0, a, 0, array.length);
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size+1);
        array[size] = e;
        size++;
        //checkMemory();
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        boolean finded = index != -1;

        if(finded)
            goBack(index);

        return finded;
//            for (int i = 0; i < size; i++) {
//                if (array[i].equals(o)) {
//                    goBack(i);
//                    deleted = true;
//                    break;
//                }
//            }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        if(c.isEmpty()) return false;
        boolean containsAll = false;
        for (Object object : c) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(object)) {
                    containsAll = true;
                    break;
                }
                else
                    containsAll = false;
            }
        }
        return containsAll;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            ensureCapacity(size+1);
            array[size] = e;
            size++;
        }
        //checkMemory();
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
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
            //checkMemory();
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
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
        for (int i = 0; i < size; i++)
            array[i] = null;
        size = 0;
    }

    /**
     * Devuelve el primer elemento de la lista, si esta vacía
     * se devuelve null.
     * @return Primer elemento de la lista.
     */

    public E getFirst(){
        checkIndex(0);
        return array[0];
    }

     /**
     * Devuelve el último elemento de la lista, si esta vacía
     * se devuelve null.
     * @return Último elemento de la lista.
     */

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
        checkIndex(index);
        E e = get(index);
        array[index] = element;
        return e;
    }

    @Override
    public void add(int index, E element) {
        checkIndex(index);
        openSpaceAt(index);
        array[index] = element;
    }

    @Override
    public E remove(int index) {
        checkIndex(index);
        E e = array[index];
        goBack(index);
        return e;
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
        int index = -1;
        for (int i = 0; i < size; i++)
            if (array[i].equals(o))
                index = i;
        return index;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new ArrayListIterator<>(this);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ArrayListIterator<>(index-1, this);
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

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        boolean deleted = false;
        for (int i = 0; i < size; i++)
            if (filter.test(array[i])){
                goBack(i);
                deleted = true;
            }
        return deleted;
    }

    @Override
    public boolean offer(E e) {
        if(capacity == size)
            return false;
        array[size] = e;
        size++;
        return true;
    }

    @Override
    public E remove() {
        if(isEmpty())
            throw new NullPointerException();
        E rem = array[0];
        remove(0);
        return rem;
    }

    @Override
    public E poll() {
        if(isEmpty())
            return null;
        E rem = array[0];
        remove(0);
        return rem;
    }

    @Override
    public E element() {
        if(isEmpty())
            throw new NullPointerException();
        return array[0];
    }

    @Override
    public E peek() {
        if(isEmpty())
            return null;
        return array[0];
    }

    private void writeObject(java.io.ObjectOutputStream os) throws IOException{
        os.defaultWriteObject();
        os.writeInt(size);
        os.writeInt(capacity);
        for (int i = 0; i < size; i++)
            os.writeObject(array[i]);
    }

    private void readObject(java.io.ObjectInputStream is) throws IOException, ClassNotFoundException{
        is.defaultReadObject();
        size = is.readInt();
        capacity = is.readInt();
        array = (E[]) new Object[capacity];

        for (int i = 0; i < size; i++)
            array[i] = (E) is.readObject();
    }

//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeObject(array);
//        out.writeInt(size);
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        array = (E[]) in.readObject();
//        size = in.readInt();
//        updateCapacity();
//    }

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

    private class ArrayListIterator<E> implements ListIterator<E>{
        private int index;
        private final Array<E> list;

        public ArrayListIterator(Array<E> list) {
            index = -1;
            this.list = list;
        }

        public ArrayListIterator(int index, Array<E> list) {
            this.index = index;
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return index+1 < size;
        }

        @Override
        public E next() {
            index++;
            return list.array[index];
        }

        @Override
        public boolean hasPrevious() {
            return index >= 0 && !isEmpty();
        }

        @Override
        public E previous() {
            return (index == -1) ? null : list.array[index];
        }

        @Override
        public int nextIndex() {
            return index+1;
        }

        @Override
        public int previousIndex() {
            return index;
        }

        @Override
        public void remove() {
            list.remove(index+1);
        }

        @Override
        public void set(E e) {
            list.set(index+1, e);
        }

        @Override
        public void add(E e) {
            list.add(e);
        }

    }

}
