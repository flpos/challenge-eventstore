package net.intelie.solution;

import org.junit.Assert;
import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

public class StoreMTTest extends MultithreadedTestCase {
    private Store store;

    @Override
    public void initialize() {
        store = new Store();
    }

    public void thread1() throws InterruptedException {
        store.insert(new Event("type", 100L));
    }

    public void thread2() throws InterruptedException {
        store.insert(new Event("type", 102L));
    }

    public void thread3() throws InterruptedException {
        store.insert(new Event("type", 103L));
    }

    @Override
    public void finish() {
        Integer expected = 3;
        Integer counter = 0;
        EventIterator evit = store.query("type", 100L, 200L);

        while (evit.moveNext()) {
            counter += 1;
        }
        Assert.assertEquals(expected, counter);
    }

    @Test
    public void testCounter() throws Throwable {
        TestFramework.runManyTimes(new StoreMTTest(), 500);
    }
}