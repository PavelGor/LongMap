package de.comparus.opensource.longmap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LongMapImplTest {
    private LongMapImpl<Long> longMap = new LongMapImpl<>();

    @Before
    public void setUp()  {
        for (long i = 0; i < 10; i++) {
            longMap.put(i, i * 10);
        }
    }

    @Test
    public void put() {
        //new value
        assertEquals(10, longMap.size());
        assertEquals(null, longMap.put(11, 110L));
        assertEquals(11, longMap.size());

        //replace value
        assertEquals(Long.valueOf(110), longMap.put(11, 111L));
        assertEquals(11, longMap.size());
        assertEquals(Long.valueOf(111), longMap.get(11));

        //new value as second in existing list
        assertEquals(null, longMap.put(27, 271L));
        assertEquals(12, longMap.size());
        assertEquals(Long.valueOf(271), longMap.get(27));

        //null value
        assertEquals(null, longMap.put(28, null));
        assertEquals(13, longMap.size());
        assertEquals(null, longMap.get(28));

        //negative key
        assertEquals(null, longMap.put(-28, -28L));
        assertEquals(14, longMap.size());
        assertEquals(Long.valueOf(-28L), longMap.get(-28));
    }

    @Test
    public void get() {
        assertEquals(Long.valueOf(10), longMap.get(1));
        assertEquals(Long.valueOf(20), longMap.get(2));
        assertEquals(Long.valueOf(30), longMap.get(3));
    }

    @Test
    public void remove() {
        assertEquals(10, longMap.size());
        assertEquals(null, longMap.remove(12));
        assertEquals(10, longMap.size());

        assertEquals(Long.valueOf(90), longMap.remove(9));
        assertEquals(9, longMap.size());
    }

    @Test
    public void ClearAndIsEmpty() {
        assertFalse(longMap.isEmpty());
        longMap.clear();
        assertTrue(longMap.isEmpty());
        assertNotEquals(Long.valueOf(10), longMap.get(1));
    }

    @Test
    public void containsKey() {
        assertTrue(longMap.containsKey(1));
        assertFalse(longMap.containsKey(12));
        longMap.put(13, null);
        assertTrue(longMap.containsKey(13));
    }

    @Test
    public void containsValue() {
        assertTrue(longMap.containsValue(10L));
        assertFalse(longMap.containsValue(120L));
        longMap.put(13, null);
        assertTrue(longMap.containsValue(null));
    }

    @Test
    public void keys() {
        long[] keys = longMap.keys();
        assertEquals(10, keys.length);
        assertEquals(1L, keys[1]);
    }

    @Test
    public void values() {
        Long[] values = longMap.values();
        assertEquals(10, values.length);
        assertEquals(Long.valueOf(10L), values[1]);
    }
}