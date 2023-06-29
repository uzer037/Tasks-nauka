import java.util.*;
import java.util.function.Consumer;

public class MyList<T> {

    private ArrayList<T> list;
    private int _size;
    private int MIN_CAPACITY = 0;
    final String nullExceptionText = "Insertion of null is not supported";

    public MyList() {
        list = new ArrayList<>();
        _size = 0;
    }
    public MyList(int capacity) {
        list = new ArrayList<>(capacity);
        _size = 0;
    }
    public MyList(Collection <T> collection) {
        if(collection.contains(null)) {
            throw new UnsupportedOperationException(nullExceptionText);
        }

        list = new ArrayList<>(collection);
        _size = collection.size();
    }

    private ArrayList<Optional<T>> getOptionalArrayListFromCollection(Collection <T> collection)
    {
        return (ArrayList<Optional<T>>) collection.stream().map(Optional::of).toList();
    }

    public Optional<T> get(int index) {
        T e = list.get(index);
        if(e == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(e);
        }
    }

    public boolean addAll(Collection<T> c) {
        if(c.contains(null)) {
            throw new UnsupportedOperationException(nullExceptionText);
        }
        _size += c.size();
        return list.addAll(c);
    }

    public boolean addAll(int index, Collection<T> c) {
        if(c.contains(null)) {
            throw new UnsupportedOperationException(nullExceptionText);
        }

        ensureCapacity(index);
        _size += c.size();
        return list.addAll(index, c);
    }

    public boolean add(T e){
        if (e == null) {
            throw new UnsupportedOperationException(nullExceptionText);
        }

        _size += 1;
        return list.add(e);
    }

    public void add(int index, T e) {
        if(e == null) {
            throw new UnsupportedOperationException(nullExceptionText);
        }

        int diff = index - list.size();
        if (diff > 0)
        {
            ArrayList<T> fillerList = new ArrayList<>(Collections.nCopies(diff, null));
            // doing addAll() via supers' method, because we've banned nulls in our implementation
            list.addAll(list.size(),fillerList);
        }

        _size += 1;
        list.add(index, e);
    }

    public void remove(int index) {
        if(list.get(index) != null) {
            _size--;
        }
        list.remove(index); // if it is out of bounds list will throw exception by itself
    }

    public void set(int index, T e) {
        if (e == null) {
            throw new UnsupportedOperationException(nullExceptionText);
        }

        int diff = index - list.size();
        if (diff > 0)
        {
            ArrayList<T> fillerList = new ArrayList<>(Collections.nCopies(diff, null));
            // doing addAll() via supers' method, because we've banned nulls in our implementation
            list.addAll(list.size(),fillerList);
        }
        if(get(index).isEmpty()) {
            _size += 1;
        }

        list.set(index, e);
    }

    public int sizeTotal() {
        /**
         * Count of all elements in list
         */
        return list.size();
    }

    public int size() {
        /**
         * Count of non-empty elements in list
         */
        return _size;
    }
    public void clear() {
        _size = 0;
        list.clear();
    }

    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        else {
            return list.contains(o);
        }
    }

    public void ensureCapacity(int minCapacity) {
        MIN_CAPACITY = minCapacity;
        list.ensureCapacity(minCapacity);
        int diff = minCapacity - list.size();
        if (diff > 0)
        {
            ArrayList<T> fillerList = new ArrayList<>(Collections.nCopies(diff, null));
            // doing addAll() via supers' method, because we've banned nulls in our implementation
            list.addAll(list.size(),fillerList);
        }
    }

    public int indexOf(Object o) {
        if (o == null) {
            return -1;
        }
        if (o == Optional.empty()) {
            return list.indexOf(null);
        }

        return list.indexOf(o);
    }

    public boolean isEmpty() {
        /**
         * Is empty or all elements that present are empty
         */
        return _size == 0;
    }
}
