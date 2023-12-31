package fr.graynaud.logbackelasticclientappender;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;

public class ECLAProperty {

    private String name;

    private String value;

    private PatternLayout layout;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PatternLayout getLayout() {
        return layout;
    }

    public void start(Context context) {
        this.layout = new PatternLayout();
        this.layout.setContext(context);
        this.layout.setPattern(this.value);
        this.layout.setPostCompileProcessor(null);
        this.layout.start();
    }

    public String doLayout(ILoggingEvent event) {
        return this.layout.doLayout(event);
    }
}
