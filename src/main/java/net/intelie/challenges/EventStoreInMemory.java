package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Implementation of {@link EventStore}.
 *
 * Concurrent map are well suited for this scenario where you need to share a
 * collection between massive number of reads and writes operations
 * {@link ConcurrentHashMap}. 
 * I do not have writed a test to prove thread safety
 * because race conditions cannot be tested deterministically, because we not
 * have the thread scheduller controll, but the ConcurrentMap Collection can
 * handle all this through atomic operations and by never locking the entire
 * table preventing other threads manipulating it.
 * 
 * 
 * @author Lucas Britto
 */
public class EventStoreInMemory implements EventStore {

	ConcurrentMap<String, List<Event>> events = new ConcurrentHashMap<>();

	@Override
	public void insert(Event event) {
		if (events.containsKey(event.type())) {
			events.get(event.type()).add(event);
		} else {
			List<Event> evt = new ArrayList<>();
			evt.add(event);
			events.put(event.type(), evt);
		}
	}

	@Override
	public void removeAll(String type) {
		events.remove(type);
	}

	/*
	 * Realizes two binary searches log(n) in the collection. One to find the first
	 * position of the first element bigger than startTime and another to find the
	 * first element smaller than endTime. Lastly return an sublist with that
	 * indexes.
	 */
	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		ConcurrentMap<String, List<Event>> resultList = Sorting.binaryIntervalSearch(events, startTime, endTime, type);

		return new EventIteratorImpl(resultList.get(type));
	}

	//Implementation of Iterator Design Pattern
	private class EventIteratorImpl implements EventIterator {
		private List<Event> iterator;
		private Event current;
		private int position = 0;

		EventIteratorImpl(List<Event> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean moveNext() {
			if (iterator != null && position < iterator.size()) {
				current = iterator.get(position);
				position++;
			} else {
				current = null;
			}

			return current != null;
		}

		@Override
		public Event current() {
			if (current == null) {
				throw new IllegalStateException();
			}

			return current;
		}

		@Override
		public void remove() {
			events.get(current.type()).remove(--position);
		}

		@Override
		public void close() throws Exception {
			if (current == null) {
				throw new IllegalStateException();
			}

			iterator = null;
		}
	}
}
