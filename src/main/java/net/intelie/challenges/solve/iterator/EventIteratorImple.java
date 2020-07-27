package net.intelie.challenges.solve.iterator;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

import java.util.Iterator;
import java.util.Set;

public class EventIteratorImple implements EventIterator {

    private Set<Event> events;
    private Event[] iterator;

    private int position = -1;

    public EventIteratorImple(Set<Event> events, Event[] iterator) {
        this.events = events;
        this.iterator = iterator;
    }

    @Override
    public boolean moveNext() {
        position++;
        return lastMoveNext();
    }

    @Override
    public Event current() {
        validationState();

        return iterator[position];
    }

    @Override
    public void remove() {
        validationState();
        Event event = iterator[position];
        iterator[position] = null;
        events.remove(event);
    }

    /**
     * Checks whether the first move has been made.
     *
     * @return {@code true} if first move has been made.
     *
     */
    private boolean isMove() {
        return position > -1;
    }

    @Override
    public void close() throws Exception {
        iterator = null;
        events = null;
    }

    /**
     * Check if it is the last move.
     *
     * @return {@code true} if last move.
     *
     */
    private boolean lastMoveNext() {
        return iterator.length > position;
    }

    /**
     * Validation that checks if the Iterator is in the starting position.
     * Validation that checks if Iterator still has elements to navigate.
     * Validation that checks if the Iterator is closed.
     *
     * @throws IllegalStateException if the rule is violated.
     *
     */
    private void validationState() {
        if (!isMove()) {
            throw new IllegalStateException("moveNext was never called.");
        }

        if (!this.lastMoveNext()) {
            throw new IllegalStateException("moveNext last result was false.");
        }

        if (iterator == null) {
            throw new IllegalStateException("state is close.");
        }
    }
}
