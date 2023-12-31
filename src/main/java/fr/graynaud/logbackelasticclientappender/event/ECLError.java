package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ECLError {

    private final String message;

    @JsonProperty("stack_trace")
    private final String stackTrace;

    private final String type;

    public ECLError(PatternLayout exPatternLayout, ILoggingEvent event) {
        this.message = event.getThrowableProxy().getMessage();
        this.stackTrace = exPatternLayout.doLayout(event);
        this.type = event.getThrowableProxy().getClassName();
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public String getType() {
        return type;
    }
}
