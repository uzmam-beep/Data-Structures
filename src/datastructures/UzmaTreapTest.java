package datastructures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class UzmaTreapTest {
    private UzmaTreap<Integer, String> treap;

    @BeforeEach
    public void setup() {
        treap = new UzmaTreap<>();
    }

    @Test
    public void testingPutAndGet() {
        treap.put(1, "One");
        treap.put(2, "Two");
        treap.put(9, "Nine");

        assertEquals("Nine", treap.get(9));
        assertEquals("Two", treap.get(2));
        assertEquals("One", treap.get(1));
        assertNull(treap.get(15));
    }

    @Test
    public void testingRemove() {
        treap.put(1, "One");
        treap.put(2, "Two");
        treap.put(9, "Nine");

        assertEquals("Nine", treap.remove(9));
        assertNull(treap.get(9));
        assertEquals(2, treap.size());
    }


    @Test
    public void testingRemoveInternalNodeWithTwoChildren() {
        treap.put(10, "Ten");
        treap.put(5, "Five");
        treap.put(15, "Fifteen");
        treap.put(12, "Twelve");
        treap.put(17, "Seventeen");

        assertEquals("Fifteen", treap.remove(15));
        assertNull(treap.get(15));
        assertEquals(4, treap.size());
    }

    @Test
    public void testingIsEmptyandSize() {
        assertTrue(treap.isEmpty());
        treap.put(1, "One");
        assertFalse(treap.isEmpty());
        assertEquals(1, treap.size());
        treap.put(2, "Two");
        assertEquals(2, treap.size());
        treap.remove(1);
        assertEquals(1, treap.size());
    }

    @Test
    public void testingDuplicateKey() {
        treap.put(1, "One");
        treap.put(1, "NewOne");
        assertEquals("NewOne", treap.get(1));
        assertEquals(1, treap.size());
    }

    @Test
    public void testingOrder() {
        for (int i = 1; i <= 10; i++) {
            treap.put(i, "Uzma" + i);
        }

        List<Integer> actual = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : treap.entrySet()) {
            actual.add(entry.getKey());
        }

        List<Integer> expected = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expected.add(i);
        }

        assertEquals(expected, actual);
    }
}
