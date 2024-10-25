package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public record ECLThread(String name) {

    public ECLThread(ILoggingEvent event) {
        this(event.getThreadName());
    }
}
