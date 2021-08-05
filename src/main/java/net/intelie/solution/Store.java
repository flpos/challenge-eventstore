package net.intelie.solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

public class Store implements EventStore {
    private List<Event> events = new ArrayList<Event>();

    @Override
    public synchronized void insert(Event event) {
        this.events.add(event);
    }

    @Override
    public void removeAll(String type) {
        this.events.removeIf(ev -> ev.type() == type);
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        List<Event> list = new ArrayList<Event>();
        Iterator<Event> it = this.events.iterator();

        while (it.hasNext()) {
            Event ev = it.next();
            if (ev.type() == type && ev.timestamp() >= startTime && ev.timestamp() < endTime) {
                list.add(ev);
            }
        }

        return new EventIteratorImpl(list);
    }

    class EventIteratorImpl implements EventIterator {
        private List<Event> list;
        private Integer index = null;

        public EventIteratorImpl(List<Event> list) {
            this.list = list;
        }

        @Override
        public void close() throws Exception {
            this.list = null;
            this.index = null;
        }

        @Override
        public boolean moveNext() {
            if (this.index == null) {
                this.index = 0;
            } else {
                this.index += 1;
            }
            return this.index < this.list.size();
        }

        @Override
        public Event current() {
            if (this.index == null) {
                throw new IllegalStateException();
            }
            return this.list.get(this.index);
        }

        @Override
        public void remove() {
            if (this.index == null) {
                throw new IllegalStateException();
            }
            Event curr = this.current();

            this.list.remove(curr);
            synchronized (this) {
                events.remove(curr);
            }

            if (this.index > 0) {
                this.index -= 1;
            }
        }

    }
}
