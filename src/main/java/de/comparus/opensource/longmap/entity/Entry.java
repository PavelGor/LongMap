package de.comparus.opensource.longmap.entity;

public class Entry<V> {
    private long key;
    private V value;

    public Entry(long key, V value) {
        this.key = key;
        this.value = value;
    }

    public long getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }


}

