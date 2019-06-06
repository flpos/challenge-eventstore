package net.intelie.challenges;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/*
*	
*/
public class EventStoreTest {
	EventStoreInMemory eventStore = new EventStoreInMemory();
	
	Event evento0 = new Event("some_type", 0L);
    Event evento1 = new Event("some_type", 1L);
    Event evento2 = new Event("some_type", 2L);
    
    @Before
    public void setup() throws Exception {
    	eventStore.insert(evento0);
    	eventStore.insert(evento1);
    	eventStore.insert(evento2);
    }
    
    @After
    public void erase() throws Exception {
    	eventStore.removeAll("some_type");
    }
    
    @Test
    public void Should_AssertFalse_When_IteratesOverEmptyIterator() {
        EventIterator iterator = eventStore.query("some_type", 0L, 0L);

        assertFalse(iterator.moveNext());
    }

    @Test
    public void Should_AssertFalse_When_IteratesFourTimesOverThreeSizedCollection() {

        EventIterator iterator = eventStore.query("some_type", 0L, 3L);

        assertTrue(iterator.moveNext());
        assertEquals(evento0, iterator.current());
        assertTrue(iterator.moveNext());
        assertEquals(evento1, iterator.current());
        assertTrue(iterator.moveNext());
        assertEquals(evento2, iterator.current());
        assertFalse(iterator.moveNext());
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowIllegalStateException_When_MoveNextReturnOnlyFalse() {
        EventIterator iterator = eventStore.query("some_type", 0L, 0L);
        assertFalse(iterator.moveNext());

        iterator.current();
    }

    @Test(expected = IllegalStateException.class)
    public void Should_ThrowIllegalStateException_When_MoveNextNeverCalled() {
        EventIterator iterator = eventStore.query("some_type", 0L, 3L);
        iterator.current();
    }

    @Test
    public void Should_AssertFalse_When_MoveNextIsCalledOverRemovedCollection() {
        EventIterator iterator = eventStore.query("some_type", 0L, 3L);

        while (iterator.moveNext()) {
            iterator.remove();
        }
        
        iterator = eventStore.query("some_type", 0L, 3L);
        assertFalse(iterator.moveNext());
    }
    
}
