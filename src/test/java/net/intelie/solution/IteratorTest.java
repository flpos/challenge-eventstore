package net.intelie.solution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

public class IteratorTest {
    private EventIterator createIterator() {
        Store store = new Store();
        store.insert(new Event("test", 123L));
        store.insert(new Event("test", 124L));
        store.insert(new Event("test", 125L));
        return store.query("test", 100L, 200L);
    }

    @Test
    public void testCurrent() {
        try (EventIterator evit = this.createIterator()) {
            assertEquals(true, evit.moveNext());
            assertEquals(123L, evit.current().timestamp());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not throw exception");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testCurrentException() {
        EventIterator evit = this.createIterator();
        evit.current();
    }

    @Test
    public void testMoveNext() {
        try (EventIterator evit = this.createIterator()) {
            assertEquals(true, evit.moveNext());
            assertEquals(123L, evit.current().timestamp());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not throw exception");
        }
    }

    @Test
    public void testRemove() {
        try (EventIterator evit = this.createIterator()) {
            evit.moveNext();
            evit.remove();
            evit.remove();
            assertEquals(125L, evit.current().timestamp());
            evit.remove();
            assertEquals(false, evit.moveNext());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should not throw exception");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveException() {
        EventIterator evit = this.createIterator();
        evit.remove();
    }
}
