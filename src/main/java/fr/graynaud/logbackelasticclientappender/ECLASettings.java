package fr.graynaud.logbackelasticclientappender;

import java.util.List;

public class ECLASettings {

    private String url;

    private String dataStreamName = "logs";

    private boolean createIndexTemplate = false;

    private String indexTemplateName = "ecla-template";

    private String indexTemplate = "{\"_meta\":{\"description\":\"ECLA template\",\"ecs_version\":\"8.11.0\"},\"data_stream\":{},\"template\":{\"mappings\":{\"date_detection\":false,\"dynamic_templates\":[{\"strings_as_keyword\":{\"mapping\":{\"ignore_above\":1024,\"type\":\"keyword\"},\"match_mapping_type\":\"string\"}}],\"properties\":{\"@timestamp\":{\"type\":\"date\",\"format\":\"epoch_millis\"},\"ecs\":{\"properties\":{\"version\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"error\":{\"properties\":{\"message\":{\"type\":\"match_only_text\"},\"stack_trace\":{\"fields\":{\"text\":{\"type\":\"match_only_text\"}},\"type\":\"wildcard\"},\"type\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"host\":{\"properties\":{\"hostname\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"labels\":{\"type\":\"object\"},\"log\":{\"properties\":{\"level\":{\"ignore_above\":1024,\"type\":\"keyword\"},\"logger\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"message\":{\"type\":\"match_only_text\"},\"process\":{\"properties\":{\"thread\":{\"properties\":{\"name\":{\"ignore_above\":1024,\"type\":\"keyword\"}}}}},\"span\":{\"properties\":{\"id\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"tags\":{\"ignore_above\":1024,\"type\":\"keyword\"},\"trace\":{\"properties\":{\"id\":{\"ignore_above\":1024,\"type\":\"keyword\"}}},\"transaction\":{\"properties\":{\"id\":{\"ignore_above\":1024,\"type\":\"keyword\"}}}}},\"settings\":{\"index\":{\"codec\":\"best_compression\",\"mapping\":{\"total_fields\":{\"limit\":2000}}}}},\"priority\":1}";

    private String lifeCycleName = "ecla-lifecycle";

    private String lifeCycle = "{\"policy\":{\"_meta\":{\"description\":\"ECLA lifecycle policy\"},\"phases\":{\"hot\":{\"actions\":{\"rollover\":{\"max_primary_shard_size\":\"10gb\",\"max_age\":\"3d\"},\"set_priority\":{\"priority\":50}}},\"warm\":{\"min_age\":\"30d\",\"actions\":{\"shrink\":{\"number_of_shards\":1},\"forcemerge\":{\"max_num_segments\":1},\"set_priority\":{\"priority\":25}}},\"delete\":{\"min_age\":\"365d\",\"actions\":{\"delete\":{}}}}}}";

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

    public String getIndexTemplate() {
        return indexTemplate;
    }

    public void setIndexTemplate(String indexTemplate) {
        this.indexTemplate = indexTemplate;
    }

    public String getLifeCycleName() {
        return lifeCycleName;
    }

    public void setLifeCycleName(String lifeCycleName) {
        this.lifeCycleName = lifeCycleName;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
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
