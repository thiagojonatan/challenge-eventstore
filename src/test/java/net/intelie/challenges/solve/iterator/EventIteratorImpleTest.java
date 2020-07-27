package net.intelie.challenges.solve.iterator;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;
import net.intelie.challenges.solve.repository.EventStoreImple;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventIteratorImpleTest {

    private EventStore eventStore;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        eventStore = new EventStoreImple();
        Event event = new Event("start", 1519862400000l);
        eventStore.insert(event);
    }

    @Test
    void assertThrowsIllegalStateExceptionCurrenteWhithoutMoveNext() {
        EventIterator iterator = eventStore.query("start", 1519862400000l, 1519862460001l);

        assertThrows(IllegalStateException.class, () -> {
            iterator.current();
        });
    }

    @Test
    void assertThrowsIllegalStateExceptionRemoveWhithoutMoveNext() {
        EventIterator iterator = eventStore.query("start", 1519862400000l, 1519862460001l);

        assertThrows(IllegalStateException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    void assertReturnMoveNextEqualFalseAfterLastElement() {
        EventIterator iterator = eventStore.query("start", 1519862400000l, 1519862460001l);
        iterator.moveNext();

        assertEquals(false, iterator.moveNext());
    }

    @Test
    void assertReturnMoveNextEqualFalseAEmptyResult() {
        EventIterator iterator = eventStore.query("stop", 1519862400000l, 1519862460001l);

        assertEquals(false, iterator.moveNext());
    }

    @Test
    void assertThrowsIllegalStateExceptionCallCurrenteAfterClose() {
        EventIterator iterator = eventStore.query("stop", 1519862400000l, 1519862460001l);
        try {
            iterator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(IllegalStateException.class, () -> {
            iterator.current();
        });
    }

    @Test
    void assertThrowsIllegalStateExceptionCallRemoveAfterClose() {
        EventIterator iterator = eventStore.query("stop", 1519862400000l, 1519862460001l);
        try {
            iterator.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThrows(IllegalStateException.class, () -> {
            iterator.remove();
        });
    }

    @Test
    void assertRemoveElement() {

        eventStore.removeAll("start");

        Event event = new Event("stop", 1519862400000l);
        eventStore.insert(event);

        EventIterator iterator = eventStore.query("stop", 1519862400000l, 1519862460001l);

        iterator.moveNext();
        iterator.remove();

        iterator = eventStore.query("stop", 1519862400000l, 1519862460001l);

        assertEquals(false, iterator.moveNext());
    }

}