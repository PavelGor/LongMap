package de.comparus.opensource.longmap;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class LongMapImplTest {
    private LongMapImpl<String> longMap = new LongMapImpl<>();

    @Test
    public void testPutEmptyMap(){
        longMap.put(1, "str1");
        assertEquals(longMap.size(), 1);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsValue("str1"));
    }

    @Test
    public void testPutNotEmptyMapNewValue(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        assertEquals(longMap.size(), 2);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsValue("str1"));
        assertTrue(longMap.containsKey(2));
        assertTrue(longMap.containsValue("str2"));
    }

    @Test
    public void testPutNotEmptyMapReplaceValue(){
        longMap.put(1, "str1");
        longMap.put(1, "str2");
        assertEquals(longMap.size(), 1);
        assertTrue(longMap.containsKey(1));
        assertFalse(longMap.containsValue("str1"));
        assertTrue(longMap.containsValue("str2"));
    }

    @Test
    public void testPutMapInOneBucket(){
        longMap.put(1, "str1");
        longMap.put(9, "str9");
        longMap.put(17, "str17");
        assertEquals(longMap.size(), 3);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsKey(9));
        assertTrue(longMap.containsKey(17));
        assertTrue(longMap.containsValue("str1"));
        assertTrue(longMap.containsValue("str9"));
        assertTrue(longMap.containsValue("str17"));
    }

    @Test
    public void testPutNegativeKey(){
        longMap.put(1, "str1");
        longMap.put(9, "str9");
        longMap.put(-9, "str-9");
        assertEquals(longMap.size(), 3);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsKey(9));
        assertTrue(longMap.containsKey(-9));
        assertTrue(longMap.containsValue("str1"));
        assertTrue(longMap.containsValue("str9"));
        assertTrue(longMap.containsValue("str-9"));
        assertEquals("str-9", longMap.get(-9));
    }

    @Test
    public void testContainsKeyTrue(){
        longMap.put(1, "str1");
        assertTrue( longMap.containsKey(1));
    }

    @Test
    public void testContainsKeyFalse(){
        assertFalse(longMap.containsKey(1));
    }

    @Test
    public void testContainsValueTrue(){
        longMap.put(1, "str1");
        assertTrue(longMap.containsValue("str1"));
    }

    @Test
    public void testContainsValueFalse(){
        assertFalse(longMap.containsValue("str1"));
    }

    @Test
    public void testGet(){
        longMap.put(1, "str1");
        String actual = longMap.get(1);
        assertEquals(actual, "str1");
    }

    @Test
    public void testGetEmptyMap(){
        assertNull(longMap.get(1));
    }

    @Test
    public void testGetNotEmptyMapElementDoesNotExist(){
        longMap.put(1, "str1");
        assertNull(longMap.get(0));
    }

    @Test
    public void testIsEmptyTrue(){
        assertTrue(longMap.isEmpty());
    }

    @Test
    public void testIsEmptyFalse(){
        longMap.put(1, "str1");
        assertFalse(longMap.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveEmptyMap(){
        longMap.remove(0);
    }

    @Test
    public void testRemoveValidKey(){
        longMap.put(1, "str1");
        assertEquals(longMap.size(), 1);
        longMap.remove(1);
        assertEquals(longMap.size(), 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveInvalidKey(){
        longMap.put(1, "str1");
        assertEquals(longMap.size(), 1);
        longMap.remove(2);
        assertEquals(longMap.size(), 1);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsValue("str1"));
    }

    @Test
    public void testClearEmptyMap(){
        longMap.clear();
        assertEquals(longMap.size(), 0);
    }

    @Test
    public void testClearNotEmptyMap(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        assertEquals(longMap.size(), 2);
        longMap.clear();
        assertEquals(longMap.size(), 0);
    }

    @Test
    public void testIncreaseCapacity(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        longMap.put(3, "str3");
        longMap.put(4, "str4");
        longMap.put(5, "str5");
        longMap.put(6, "str6");
        assertEquals(longMap.size(), 6);
        longMap.put(7, "str7");
        assertEquals(longMap.size(), 7);
        assertTrue(longMap.containsKey(1));
        assertTrue(longMap.containsValue("str1"));
        assertTrue(longMap.containsKey(2));
        assertTrue(longMap.containsValue("str2"));
        assertTrue(longMap.containsKey(3));
        assertTrue(longMap.containsValue("str3"));
        assertTrue(longMap.containsKey(4));
        assertTrue(longMap.containsValue("str4"));
        assertTrue(longMap.containsKey(5));
        assertTrue(longMap.containsValue("str5"));
        assertTrue(longMap.containsKey(6));
        assertTrue(longMap.containsValue("str6"));
        assertTrue(longMap.containsKey(7));
        assertTrue(longMap.containsValue("str7"));
    }

    @Test
    public void testItrHasNextEmpty(){
        Iterator iterator = longMap.iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testItrNext(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        longMap.put(3, "str3");
        int i = 1;
        for (Iterator iterator = longMap.iterator(); iterator.hasNext();){
            switch (i){
                case 1:
                    assertEquals(String.valueOf(iterator.next()), "Entry{key=1, value=str1}");
                    i++;
                    break;
                case 2:
                    assertEquals(String.valueOf(iterator.next()), "Entry{key=2, value=str2}");
                    i++;
                    break;
                case 3:
                    assertEquals(String.valueOf(iterator.next()), "Entry{key=3, value=str3}");
                    break;
            }
        }

    }

    @Test
    public void testItrRemoveBegin(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        longMap.put(3, "str3");
        assertEquals(longMap.size(), 3);
        assertTrue(longMap.containsKey(1));
        longMap.remove(1);
        assertEquals(longMap.size(), 2);
        assertFalse(longMap.containsKey(1));
    }

    @Test
    public void testItrRemoveCenter(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        longMap.put(3, "str3");
        assertEquals(longMap.size(), 3);
        assertTrue(longMap.containsKey(2));
        longMap.remove(2);
        assertEquals(longMap.size(), 2);
        assertFalse(longMap.containsKey(2));
    }

    @Test
    public void testItrRemoveEnd(){
        longMap.put(1, "str1");
        longMap.put(2, "str2");
        longMap.put(3, "str3");
        assertEquals(longMap.size(), 3);
        assertTrue(longMap.containsKey(3));
        longMap.remove(3);
        assertEquals(longMap.size(), 2);
        assertFalse(longMap.containsKey(3));
    }

    @Test
    public void testGetKeys() {
        for (long i = 0; i < 10; i++) {
            longMap.put(i, "str"+i);
        }
        long[] keys = longMap.keys();
        assertEquals(10, keys.length);
        assertEquals(1L, keys[1]);
    }

    @Test
    public void testGetValues() {
        for (long i = 0; i < 10; i++) {
            longMap.put(i, "str"+i);
        }
        String[] values = longMap.values();
        assertEquals(10, values.length);
        assertEquals("str1", values[1]);
    }
}