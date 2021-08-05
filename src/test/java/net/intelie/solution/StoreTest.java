package net.intelie.solution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

public class StoreTest {
    @Test
    public void testInsert() {
        Store store = new Store();
        store.insert(new Event("test", 123L));
        EventIterator evit = store.query("test", 122L, 124L);
        evit.moveNext();
        assertEquals("test", evit.current().type());
        assertEquals(123L, evit.current().timestamp());
    }

    @Test
    public void testQuery() {
        Store store = new Store();
        store.insert(new Event("test", 123L));
        store.insert(new Event("test", 100L));
        store.insert(new Event("query", 124L));
        store.insert(new Event("test", 123L));
        store.insert(new Event("test", 200L));
        store.insert(new Event("test", 223L));

        EventIterator evit = store.query("test", 100L, 200L);

        while (evit.moveNext()) {
            Event ev = evit.current();
            assertEquals("test", ev.type());
            assertTrue(ev.timestamp() >= 100L);
            assertTrue(ev.timestamp() < 200L);
        }
    }

    @Test
    public void testRemoveAll() {
        Store store = new Store();
        store.insert(new Event("test", 123L));
        store.insert(new Event("test", 100L));
        store.insert(new Event("query", 124L));

        store.removeAll("test");
        EventIterator evitTest = store.query("test", 100, 200);
        assertFalse(evitTest.moveNext());

        EventIterator evitQuery = store.query("query", 100, 200);
        assertTrue(evitQuery.moveNext());
    }
}
