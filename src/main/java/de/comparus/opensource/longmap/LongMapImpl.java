package de.comparus.opensource.longmap;

import de.comparus.opensource.longmap.entity.Entry;

import java.lang.reflect.Array;
import java.util.LinkedList;

public class LongMapImpl<V> implements LongMap<V> {

    private int capacity = 8;
    private double loadFactor = 0.75;
    private LinkedList<Entry<V>>[] buckets;
    private int size;

    public LongMapImpl() {
        init();
    }

    public LongMapImpl(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        init();
    }

    @SuppressWarnings("unchecked")
    private void init() {
        buckets = new LinkedList[capacity];
    }

    public V put(long key, V value) {
        if (size >= loadFactor * capacity) {
            increaseCapacity();
        }

        Entry<V> newEntry = new Entry<>(key, value);
        Entry<V> entryToUpdate = getEntry(key);

        if (entryToUpdate != null) {
            V oldValue = entryToUpdate.getValue();
            entryToUpdate.setValue(value);
            return oldValue;
        }

        int index = getBucketIndex(key);
        if (buckets[index] == null) {
            buckets[index] = new LinkedList<>();
        }

        buckets[index].add(newEntry);
        size++;
        return null;
    }

    public V get(long key) {
        if (size > 0) {
            Entry<V> currentEntry = getEntry(key);
            if (currentEntry != null) {
                return currentEntry.getValue();
            }
        }
        return null;
    }

    public V remove(long key) {
        if (size > 0) {
            Entry<V> entryToRemove = getEntry(key);

            if (entryToRemove != null) {
                buckets[getBucketIndex(key)].remove(entryToRemove);
                size--;
                return entryToRemove.getValue();
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        return getEntry(key) != null;
    }

    public boolean containsValue(V value) {
        if (size > 0) {
            for (LinkedList<Entry<V>> bucket : buckets) {
                if (bucket != null) {
                    for (Entry<V> element : bucket) {
                        if (element.getValue() == value ||
                                (value != null && value.equals(element.getValue())))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public long[] keys() {
        if (size > 0) {
            long[] keys = new long[size];
            int index = 0;

            for (LinkedList<Entry<V>> bucket : buckets) {
                if (bucket != null) {
                    for (Entry<V> element : bucket) {
                        keys[index] = element.getKey();
                        index++;
                    }
                }
            }
            return keys;
        }
        return null;
    }

    public V[] values() {
        if (size > 0) {
            int index = 0;

            @SuppressWarnings("unchecked")
            V[] values = (V[]) new Object[size];

            for (LinkedList<Entry<V>> bucket : buckets) {
                if (bucket != null) {
                    for (Entry<V> element : bucket) {
                        values[index] = element.getValue();
                        index++;
                    }
                }
            }

            @SuppressWarnings("unchecked")
            V[] vTypeValues = getGenericArray((Class<V>) values[0].getClass(), size);
            System.arraycopy(values, 0, vTypeValues, 0, values.length);
            return vTypeValues;
        }
        return null;
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
        capacity *= 2;

        @SuppressWarnings("unchecked")
        LinkedList<Entry<V>>[] newBuckets = new LinkedList[capacity];

        for (LinkedList<Entry<V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<V> element : bucket) {
                    int index = getBucketIndex(element.getKey());
                    if (newBuckets[index] == null) {
                        newBuckets[index] = new LinkedList<>();
                    }
                    newBuckets[index].add(element);
                }
            }
        }
        buckets = newBuckets;
    }

    private int getBucketIndex(long key) {
        return (int) Math.abs(key % capacity);
    }

    private Entry<V> getEntry(long key) {
        if (size > 0) {
            LinkedList<Entry<V>> bucket = buckets[getBucketIndex(key)];
            if (bucket != null) {
                for (Entry<V> element : bucket) {
                    if (key == element.getKey()) {
                        return element;
                    }
                }
            }
        }
        return null;
    }

    private <E> E[] getGenericArray(Class<E> clazz, int size) {
        @SuppressWarnings("unchecked")
        E[] arr = (E[]) Array.newInstance(clazz, size);
        return arr;
    }
}
