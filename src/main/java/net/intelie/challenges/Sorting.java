package net.intelie.challenges;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class Sorting {
	
	// Find index >= x
	private static int getLowerIndex(List<Event> arr, long x) {
		int l = 0, h = arr.size() - 1; 
        while (l <= h)  
        { 
            int mid = (l + h) / 2; 
            if (arr.get(mid).timestamp() >= x) 
                h = mid - 1; 
            else
                l = mid + 1; 
        } 
        return l; 
	}

	// Find index < y
	private static int getUpperIndex(List<Event> arr, long y) {
		int l = 0, h = arr.size() - 1; 
        while (l <= h)  
        { 
            int mid = (l + h) / 2; 
            if (arr.get(mid).timestamp() < y) 
                l = mid + 1; 
            else
                h = mid - 1; 
        } 
        return h;
	}

	// Query elements
	public static ConcurrentMap<String, List<Event>> binaryIntervalSearch(ConcurrentMap<String, List<Event>> events, long x, long y, String type) {
		int lowerIndex = getLowerIndex(events.get(type), x);
		int upperIndex = getUpperIndex(events.get(type), y);

		if(lowerIndex < upperIndex) {
			events.get(type).subList(lowerIndex, upperIndex);
		}else {
			events.get(type).clear();
		}
		
		return events;
	}
}
