package fr.graynaud.logbackelasticclientappender.event;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.graynaud.logbackelasticclientappender.ECLAProperties;
import fr.graynaud.logbackelasticclientappender.ECLAProperty;
import fr.graynaud.logbackelasticclientappender.ECLASettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ECLEvent {

    private static final ECLEcs ECS = ECLEcs.INSTANCE;

    @JsonProperty("@timestamp")
    private final long timestamp;

    private final String message;

    private final List<String> tags;

    private final Map<String, String> labels;

    private final ECLLog log;

    private final ECLHost host;

    private final ECLError error;

    private final ECLProcess process;

    private final ECLId trace;

    private final ECLId span;

    private final ECLId transaction;

    public ECLEvent(ILoggingEvent event, ECLASettings settings, String host, PatternLayout exPatternLayout, ECLAProperties customProperties) {
        this.timestamp = event.getTimeStamp();
        this.message = event.getFormattedMessage();
        this.tags = settings.getTags();
        this.log = new ECLLog(event);
        this.host = new ECLHost(host);
        this.error = (exPatternLayout == null || event.getThrowableProxy() == null) ? null : new ECLError(exPatternLayout, event);
        this.process = new ECLProcess(event);

        Map<String, String> mdc;

        if (settings.isAddMDC()) {
            mdc = event.getMDCPropertyMap();
            this.trace = settings.getTraceIdField() == null ? null : new ECLId(mdc.get(settings.getTraceIdField()));
            this.span = settings.getSpanIdField() == null ? null : new ECLId(mdc.get(settings.getSpanIdField()));
            this.transaction = settings.getTransactionIdField() == null ? null : new ECLId(mdc.get(settings.getTransactionIdField()));
        } else {
            mdc = null;
            this.trace = null;
            this.span = null;
            this.transaction = null;
        }

        if ((mdc != null && !mdc.isEmpty()) || (customProperties != null && !customProperties.getProperties().isEmpty())) {
            int size = 0;

            if (mdc != null && !mdc.isEmpty()) {
                size += mdc.size();
            }

            if ((customProperties != null && !customProperties.getProperties().isEmpty())) {
                size += customProperties.getProperties().size();
            }

            this.labels = new HashMap<>(size);

            if (mdc != null && !mdc.isEmpty()) {
                this.labels.putAll(mdc);
            }

            if (customProperties != null && !customProperties.getProperties().isEmpty()) {
                for (ECLAProperty property : customProperties.getProperties()) {
                    this.labels.put(property.getName(), property.doLayout(event));
                }
            }
        } else {
            this.labels = null;
        }
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getTags() {
        return tags;
    }

    public ECLLog getLog() {
        return log;
    }

    public ECLHost getHost() {
        return host;
    }

    public ECLError getError() {
        return error;
    }

    @JsonProperty("ecs")
    public ECLEcs getEcs() {
        return ECS;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public ECLProcess getProcess() {
        return process;
    }

    public ECLId getTrace() {
        return trace;
    }

    public ECLId getSpan() {
        return span;
    }

    public ECLId getTransaction() {
        return transaction;
    }
}
