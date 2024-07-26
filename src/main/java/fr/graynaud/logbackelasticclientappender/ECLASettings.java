package fr.graynaud.logbackelasticclientappender;

import java.util.List;

public class ECLASettings {

    private String url;

    private String dataStreamName = "logs";

    private boolean createIndexTemplate = false;

    private String indexTemplateName = "ecla-template";

    private String indexTemplateLocation = "/fr/graynaud/logbackelasticclientappender/template.json";

    private String lifeCycleName = "ecla-lifecycle";

    private String lifeCycleLocation = "/fr/graynaud/logbackelasticclientappender/lifecycle.json";

    private ECLATags tags;

    private String hostname;

    private String pipeline;

    private int connectTimeout = 30_000;

    private int socketTimeout = 30_000;

    private ExPattern exPattern = ExPattern.EX;

    private int flushCount = 1_000;

    private Integer flushInterval;

    private Long flushBytes;

    private boolean addMDC = false;

    private String traceIdField;

    private String spanIdField;

    private String transactionIdField;

    public enum ExPattern {
        EX("ex"), REX("rEx"), XEX("xEx");

        public final String pattern;

        ExPattern(String pattern) {
            this.pattern = pattern;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = ECLAUtils.cleanStringSetting(url);
    }

    public String getDataStreamName() {
        return dataStreamName;
    }

    public void setDataStreamName(String dataStreamName) {
        this.dataStreamName = ECLAUtils.cleanStringSetting(dataStreamName);
    }

    public boolean isCreateIndexTemplate() {
        return createIndexTemplate;
    }

    public void setCreateIndexTemplate(boolean createIndexTemplate) {
        this.createIndexTemplate = createIndexTemplate;
    }

    public String getIndexTemplateName() {
        return indexTemplateName;
    }

    public void setIndexTemplateName(String indexTemplateName) {
        this.indexTemplateName = indexTemplateName;
    }

    public String getIndexTemplateLocation() {
        return indexTemplateLocation;
    }

    public void setIndexTemplateLocation(String indexTemplateLocation) {
        this.indexTemplateLocation = indexTemplateLocation;
    }

    public String getLifeCycleName() {
        return lifeCycleName;
    }

    public void setLifeCycleName(String lifeCycleName) {
        this.lifeCycleName = lifeCycleName;
    }

    public String getLifeCycleLocation() {
        return lifeCycleLocation;
    }

    public void setLifeCycleLocation(String lifeCycleLocation) {
        this.lifeCycleLocation = lifeCycleLocation;
    }

    public List<String> getTags() {
        return this.tags == null ? null : this.tags.getTags();
    }

    public void setTags(ECLATags tags) {
        this.tags = tags;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = ECLAUtils.cleanStringSetting(hostname);
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = ECLAUtils.cleanStringSetting(pipeline);
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public ExPattern getExPattern() {
        return exPattern;
    }

    public void setExPattern(ExPattern exPattern) {
        this.exPattern = exPattern;
    }

    public int getFlushCount() {
        return flushCount;
    }

    public void setFlushCount(int flushCount) {
        this.flushCount = flushCount;
    }

    public Integer getFlushInterval() {
        return flushInterval;
    }

    public void setFlushInterval(Integer flushInterval) {
        this.flushInterval = flushInterval;
    }

    public Long getFlushBytes() {
        return flushBytes;
    }

    public void setFlushBytes(Long flushBytes) {
        this.flushBytes = flushBytes;
    }

    public boolean isAddMDC() {
        return addMDC;
    }

    public void setAddMDC(boolean addMDC) {
        this.addMDC = addMDC;
    }

    public String getTraceIdField() {
        return traceIdField;
    }

    public void setTraceIdField(String traceIdField) {
        this.traceIdField = ECLAUtils.cleanStringSetting(traceIdField);
    }

    public String getSpanIdField() {
        return spanIdField;
    }

    public void setSpanIdField(String spanIdField) {
        this.spanIdField = ECLAUtils.cleanStringSetting(spanIdField);
    }

    public String getTransactionIdField() {
        return transactionIdField;
    }

    public void setTransactionIdField(String transactionIdField) {
        this.transactionIdField = ECLAUtils.cleanStringSetting(transactionIdField);
    }
}
