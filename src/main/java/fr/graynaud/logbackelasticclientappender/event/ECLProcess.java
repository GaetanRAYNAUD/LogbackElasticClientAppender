package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public record ECLProcess(ECLThread thread) {

    public ECLProcess(ILoggingEvent event) {
        this(new ECLThread(event));
    }
}
