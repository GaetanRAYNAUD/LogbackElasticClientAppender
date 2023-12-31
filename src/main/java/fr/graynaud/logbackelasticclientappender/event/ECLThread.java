package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class ECLThread {

    private final String name;

    public ECLThread(ILoggingEvent event) {
        this.name = event.getThreadName();
    }

    public String getName() {
        return name;
    }
}
