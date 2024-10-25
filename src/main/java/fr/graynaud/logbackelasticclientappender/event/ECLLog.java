package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public record ECLLog(String level, String logger) {

    public ECLLog(ILoggingEvent event) {
        this(event.getLevel().toString(), event.getLoggerName());
    }
}
