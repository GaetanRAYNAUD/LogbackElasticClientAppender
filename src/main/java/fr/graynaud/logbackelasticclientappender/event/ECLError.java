package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ECLError(String message, @JsonProperty("stack_trace") String stackTrace, String type) {

    public ECLError(PatternLayout exPatternLayout, ILoggingEvent event) {
        this(event.getThrowableProxy().getMessage(), exPatternLayout.doLayout(event), event.getThrowableProxy().getClassName());
    }
}
