# Challenge

Create a class that implements the `EventStore` interface.
 
# Solution

The solution is implemented using nested classes, for direct access to the event list.

I used ArrayList for the event list, for it's simplicity.

The insert method of the Store class is synchronized, preventing concurrency problems.

The EventList iterator maintains a list of the queried events, later in the development i encountered some problems, probably just iterating on the events list and dealing with the missing events (after removal) would be better.

I learned a lot in this challenge, but i couldn't provide a better solution for the concurrency problems in the challenge time, specially for the removal of events.

# Dependencies

I installed one dependency to help me test concurrency, the `multithreadedtc`.
