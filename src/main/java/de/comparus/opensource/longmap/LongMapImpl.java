package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LongMapImpl<V> implements LongMap<V>, Iterable<LongMapImpl<V>.Entry<V>> {
    private final static int DEFAULT_INITIAL_CAPACITY = 8;
    private final static double DEFAULT_LOAD_FACTOR = 0.75;

    private double loadFactor;
    private Entry<V>[] buckets;
    private int size;

    public LongMapImpl() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public LongMapImpl(int capacity, double loadFactor) {
        this.loadFactor = loadFactor;
        buckets = new Entry[capacity];
    }

    public V put(long key, V value) {
        if (size >= loadFactor * buckets.length) {
            increaseCapacity();
        }

        Entry<V> newEntry = new Entry<>(key, value, null);
        Entry<V> entryToUpdate = getEntry(key);

        if (entryToUpdate != null) {
            V oldValue = entryToUpdate.value;
            entryToUpdate.value = value;
            return oldValue;
        }

        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = newEntry;
        } else {
            Entry<V> vEntry = buckets[index];
            while (vEntry.next != null) {
                vEntry = vEntry.next;
            }
            vEntry.next = newEntry;
        }
        size++;
        return null;
    }

    public V get(long key) {
        Entry<V> currentEntry = getEntry(key);
        if (currentEntry != null) {
            return currentEntry.value;
        }
        return null;
    }

    public V remove(long key) {
        for (Iterator<Entry<V>> iterator = this.iterator(); iterator.hasNext(); ) {
            Entry<V> entry = iterator.next();
            if (entry.key == key) {
                iterator.remove();
                size--;
                return entry.value;
            }
        }
        throw new NoSuchElementException("No element were found with key = " + key);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        return getEntry(key) != null;
    }

    public boolean containsValue(V value) {
        for (Entry<V> entry : this) {
            if (entry.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public long[] keys() {
        long[] keys = new long[size];
        int index = 0;

        for (Entry<V> entry : this) {
            keys[index] = entry.key;
            index++;
        }

        return keys;
    }

    public V[] values() {
        int index = 0;

        @SuppressWarnings("unchecked")
        V[] values = (V[]) new Object[size];

        for (Entry<V> entry : this) {
            values[index] = entry.value;
            index++;
        }

        @SuppressWarnings("unchecked")
        V[] vTypeValues = getGenericArray((Class<V>) values[0].getClass(), size);
        System.arraycopy(values, 0, vTypeValues, 0, values.length);
        return vTypeValues;
    }

    public long size() {
        return size;
    }

    public void clear() {
        if (size > 0) {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = null;
            }
        }
        size = 0;
    }

    private void increaseCapacity() {
        int newCapacity = buckets.length * 2;

        @SuppressWarnings("unchecked")
        Entry<V>[] newBuckets = new Entry[newCapacity];

        for (Entry<V> entry : this) {
            int index = getBucketIndex(entry.key);
            if (newBuckets[index] == null) {
                newBuckets[index] = entry;
            } else {
                Entry<V> listEntry = buckets[index];
                while (listEntry.next != null) {
                    listEntry = listEntry.next;
                }
                listEntry.next = entry;
            }
        }

        buckets = newBuckets;
    }

    private int getBucketIndex(long key) {
        return (int) Math.abs(key % buckets.length);
    }

    private Entry<V> getEntry(long key) {
        for (Entry<V> entry : this) {
            if (entry.key == key) {
                return entry;
            }
        }
        return null;
    }

    private <E> E[] getGenericArray(Class<E> clazz, int size) {
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) Array.newInstance(clazz, size);
        return arr;
    }

    @Override
    public Iterator<Entry<V>> iterator() {
        return new LongMapIterator();
    }

    class LongMapIterator implements Iterator<Entry<V>> {

        int index;
        int bucketIndex;
        Entry<V> nextEntry;
        Entry<V> prevEntry;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Entry<V> next() {
            if (!hasNext()) {
                throw new IllegalStateException();
            }

            if (nextEntry != null) {
                Entry<V> currentEntry = nextEntry;
                nextEntry = nextEntry.next;
                index++;
                return currentEntry;
            }

            Entry<V> bucket = buckets[bucketIndex];
            while (bucket == null) {
                bucketIndex++;
                bucket = buckets[bucketIndex];
            }

            Entry<V> entry = bucket;
            if (entry.next == null) {
                bucketIndex++;
                prevEntry = null;
            } else {
                nextEntry = entry.next;
                prevEntry = entry;
            }

            index++;
            return entry;
        }

        public void remove() {
            if (prevEntry == null) {
                buckets[bucketIndex - 1] = null;
            } else {
                prevEntry.next = null;
            }
            index--;
        }
    }

    class Entry<E> {
        private long key;
        private E value;
        private Entry<E> next;

        Entry(long key, E value, Entry<E> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

}
