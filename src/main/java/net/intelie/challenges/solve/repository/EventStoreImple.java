package net.intelie.challenges.solve.repository;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;
import net.intelie.challenges.solve.iterator.EventIteratorImple;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class EventStoreImple implements EventStore {

    private Set<Event> events = Collections.synchronizedSet(new HashSet<>());

    @Override
    public synchronized void insert(Event event) {
        events.add(event);
    }

    @Override
    public void removeAll(String type) {
        events.removeAll(events.stream().filter(item -> item.type().equals(type)).collect(Collectors.toSet()));
    }

    @Override
    public synchronized EventIterator query(String type, long startTime, long endTime) {
        if(type == null)
            throw new IllegalArgumentException("Check parameters");

        List<Event> eventsResult = events.stream().filter(item -> item.type().equals(type))
                .filter(
                        item -> LongStream.range(startTime, endTime).boxed().collect(Collectors.toList()).contains(item.timestamp())
                ).collect(Collectors.toList());

        int size = eventsResult.size();
        Event[] result = new Event[size];

        for(int x = 0; x < size; x++) {
            result[x] = eventsResult.get(x);
        }

        return new EventIteratorImple(events, result);
    }
}
