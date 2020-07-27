package net.intelie.challenges;

import java.util.Comparator;
import java.util.Objects;

/**
 * This is just an event stub, feel free to expand it if needed.
 */
public class Event {
    private final String type;
    private final long timestamp;
    private String os;
    private String browser;
    private float minTimeResponse;
    private float maxTimeResponse;

    public Event(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }
    public Event(String type, long timestamp, String os, String browser, float minTimeResponse, float maxTimeResponse) {
        this(type, timestamp);
        this.os = os;
        this.browser = browser;
        this.minTimeResponse = minTimeResponse;
        this.maxTimeResponse = maxTimeResponse;
    }

    public String type() {
        return type;
    }

    public long timestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return timestamp == event.timestamp &&
                Double.compare(event.minTimeResponse, minTimeResponse) == 0 &&
                Double.compare(event.maxTimeResponse, maxTimeResponse) == 0 &&
                Objects.equals(type, event.type) &&
                Objects.equals(os, event.os) &&
                Objects.equals(browser, event.browser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, timestamp, os, browser, minTimeResponse, maxTimeResponse);
    }
}
