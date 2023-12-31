package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class ECLLog {

    private final String level;

    private final String logger;

    public ECLLog(ILoggingEvent event) {
        this.level = event.getLevel().toString();
        this.logger = event.getLoggerName();
    }

    public String getLevel() {
        return level;
    }

    public String getLogger() {
        return logger;
    }
}
