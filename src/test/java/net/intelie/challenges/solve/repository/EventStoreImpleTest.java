package net.intelie.challenges.solve.repository;


import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class EventStoreImpleTest {

    private EventStore eventStore;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        eventStore = new EventStoreImple();
        Event event = new Event("start", 1519862400000l);
        eventStore.insert(event);
        event = new Event("span", 1519862400000l);
        eventStore.insert(event);
        event = new Event("data", 1519862400000l, "linux", "chrome", 0.1f, 1.3f);
        eventStore.insert(event);
        event = new Event("data", 1519862400000l, "mac", "chrome", 0.2f, 1.2f);
        eventStore.insert(event);
        event = new Event("data", 1519862400000l, "mac", "firefox", 0.3f, 1.2f);
        eventStore.insert(event);
        event = new Event("data", 1519862400000l, "linux", "firefox", 0.1f, 1.0f);
        eventStore.insert(event);
        event = new Event("data", 1519862460000l, "linux", "chrome", 0.2f, 0.9f);
        eventStore.insert(event);
        event = new Event("data", 1519862460000l, "mac", "chrome", 0.1f, 1.0f);
        eventStore.insert(event);
        event = new Event("data", 1519862460000l, "mac", "firefox", 0.2f, 1.1f);
        eventStore.insert(event);
        event = new Event("data", 1519862460000l, "linux", "firefox", 0.3f, 1.4f);
        eventStore.insert(event);
        event = new Event("stop", 1519862460000l);
        eventStore.insert(event);
    }

    @Test
    void assertAmountDataEvent() {
        EventIterator iterator = eventStore.query("data", 1519862400000l, 1519862460001l);
        int amount = 0;
        while(iterator.moveNext()) {
            amount++;
        }

        assertEquals(8, amount);
    }

    @Test
    void assertRemoveDataEvent() {
        eventStore.removeAll("data");

        EventIterator iterator = eventStore.query("data", 1519862400000l, 1519862460001l);
        int amount = 0;
        while(iterator.moveNext()) {
            amount++;
        }

        assertEquals(0, amount);
    }

    @Test
    void assertRemoveCorrectEvent() {
        eventStore.removeAll("data");

        EventIterator iterator = eventStore.query("span", 1519862400000l, 1519862460001l);
        int amount = 0;
        while(iterator.moveNext()) {
            amount++;
        }

        assertEquals(1, amount);
    }


    @Test
    public void testWithConcurrencyAccessThreadSleep() throws InterruptedException {
        eventStore.removeAll("data");

        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                long mili = System.currentTimeMillis();
                double random = Math.random();
                String value = mili + " - " + random;
                Event event = new Event("data", 1519862400000l, "linux" + "_" + value, "chrome", 0.1f, 1.3f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "mac" + "_" + value, "chrome", 0.2f, 1.2f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "mac"+ "_" + value, "firefox", 0.3f, 1.2f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "linux"+ "_" + value, "firefox", 0.1f, 1.0f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "linux"+ "_" + value, "chrome", 0.2f, 0.9f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "mac"+ "_" + value, "chrome", 0.1f, 1.0f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "mac"+ "_" + value, "firefox", 0.2f, 1.1f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "linux"+ "_" + value, "firefox", 0.3f, 1.4f);
                eventStore.insert(event);
            });
        }

        //Time to add
        Thread.sleep(1000);

        EventIterator iterator = eventStore.query("data", 1519862400000l, 1519862460001l);
        int amount = 0;
        while(iterator.moveNext()) {
            amount++;
        }

        assertEquals(numberOfThreads * 8, amount);
    }

    @Test
    public void testWithConcurrencyAccessWhithoutThreadSleep() throws InterruptedException {
        eventStore.removeAll("data");

        int numberOfThreads = 100;
        ExecutorService service = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numberOfThreads; i++) {
            service.execute(() -> {
                long mili = System.currentTimeMillis();
                double random = Math.random();
                String value = mili + " - " + random;
                Event event = new Event("data", 1519862400000l, "linux" + "_" + value, "chrome", 0.1f, 1.3f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "mac" + "_" + value, "chrome", 0.2f, 1.2f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "mac"+ "_" + value, "firefox", 0.3f, 1.2f);
                eventStore.insert(event);
                event = new Event("data", 1519862400000l, "linux"+ "_" + value, "firefox", 0.1f, 1.0f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "linux"+ "_" + value, "chrome", 0.2f, 0.9f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "mac"+ "_" + value, "chrome", 0.1f, 1.0f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "mac"+ "_" + value, "firefox", 0.2f, 1.1f);
                eventStore.insert(event);
                event = new Event("data", 1519862460000l, "linux"+ "_" + value, "firefox", 0.3f, 1.4f);
                eventStore.insert(event);
            });
        }

        EventIterator iterator = eventStore.query("data", 1519862400000l, 1519862460001l);
        int amount = 0;
        while(iterator.moveNext()) {
            amount++;
        }

        assertEquals(0, amount);
    }
}