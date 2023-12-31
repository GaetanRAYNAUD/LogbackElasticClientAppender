package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class ECLProcess {

    private final ECLThread thread;

    public ECLProcess(ILoggingEvent event) {
        this.thread = new ECLThread(event);
    }

    public ECLThread getThread() {
        return thread;
    }
}
