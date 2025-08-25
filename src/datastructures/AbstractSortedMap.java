package datastructures;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.SortedMap;


public abstract class AbstractSortedMap<K,V> extends AbstractMap<K,V> implements SortedMap<K,V> {

    protected Comparator<K> comp;

    protected AbstractSortedMap(Comparator<K> c) {
        comp = c;
    }

    protected AbstractSortedMap() {
        this(new MyDefaultComparator<K>());
    }

    @Override
    public Comparator<? super K> comparator() {
        return comp;
    }

    protected Comparator<K> internalComparator() {
        return comp;
    }

    protected int compare(Entry<K,V> a, Entry<K,V> b) {
        return comp.compare(a.getKey(), b.getKey());
    }

    protected int compare(K key, Entry<K,V> entry) {
        return comp.compare(key, entry.getKey());
    }

    protected int compare(Entry<K,V> entry, K key) {
        return comp.compare(entry.getKey(), key);
    }

    protected int compare(K a, K b) {
        return comp.compare(a, b);
    }

    protected void checkKey(K key) throws IllegalArgumentException {
        try {
            comp.compare(key, key);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Incompatible key type: " + key.getClass());
        }
    }

    @Override
    public abstract SortedMap<K,V> subMap(K fromKey, K toKey);

    @Override
    public abstract SortedMap<K,V> headMap(K toKey);

    @Override
    public abstract SortedMap<K,V> tailMap(K fromKey);

    @Override
    public abstract K firstKey();

    @Override
    public abstract K lastKey();
}
