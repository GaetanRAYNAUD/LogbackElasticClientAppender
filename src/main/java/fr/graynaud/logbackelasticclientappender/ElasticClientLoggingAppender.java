package fr.graynaud.logbackelasticclientappender;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.elasticsearch.ilm.ElasticsearchIlmClient;
import co.elastic.clients.elasticsearch.ilm.PutLifecycleResponse;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import fr.graynaud.logbackelasticclientappender.authentication.Authentication;
import fr.graynaud.logbackelasticclientappender.event.ECLEvent;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ElasticClientLoggingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private Authentication authentication = null;

    private ECLASettings settings;

    private ECLAProperties customProperties;

    private PatternLayout exPatternLayout;

    private String hostname;

    private ElasticsearchAsyncClient client;

    private BulkIngester<Void> bulkIngester;

    public ElasticClientLoggingAppender() {
        this(new ECLASettings());
    }

    public ElasticClientLoggingAppender(ECLASettings settings) {
        this.settings = settings;
    }

    @Override
    public void start() {
        ObjectMapper objectMapper = JsonMapper.builder()
                                              .enable(JsonWriteFeature.QUOTE_FIELD_NAMES)
                                              .build()
                                              .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY);

        RestClient restClient = RestClient.builder(HttpHost.create(this.settings.getUrl()))
                                          .setRequestConfigCallback(b -> b.setConnectionRequestTimeout(this.settings.getConnectTimeout())
                                                                          .setSocketTimeout(this.settings.getSocketTimeout()))
                                          .setHttpClientConfigCallback(b -> b.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build())
                                                                             .setDefaultHeaders(this.authentication == null ? null : List.of(
                                                                                     new BasicHeader("Authorization",
                                                                                                     this.authentication.getAuthorizationHeader()))))
                                          .build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));
        this.client = new ElasticsearchAsyncClient(transport);

        this.hostname = this.settings.getHostname();

        if (this.hostname == null) {
            if (System.getProperty("os.name").startsWith("Windows")) {
                this.hostname = System.getenv("COMPUTERNAME");
            } else {
                this.hostname = System.getenv("HOSTNAME");
            }

            if (this.hostname == null) {
                try {
                    Process proc = Runtime.getRuntime().exec("hostname");
                    try (BufferedReader stream = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                        this.hostname = stream.readLine();
                    }
                } catch (Exception e) {
                    addError("An error occurred while reading hostname", e);
                }
            }
        }

        if (this.settings.getExPattern() != null) {
            this.exPatternLayout = new PatternLayout();
            this.exPatternLayout.setContext(getContext());
            this.exPatternLayout.setPattern(this.settings.getExPattern().pattern);
            this.exPatternLayout.start();
        }

        BulkListener<Void> listener = new BulkListener<>() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request, List<Void> contexts) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, List<Void> contexts, BulkResponse response) {
                if (response.errors()) {
                    for (BulkResponseItem item : response.items()) {
                        if (item.error() != null) {
                            addError("An error occurred while sending log " + item.id() + " to elastic: " + item.error().reason());
                        }
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request, List<Void> contexts, Throwable failure) {
                addError("An error occurred while sending log to elastic for " + executionId + " with " + request.operations().size() + " actions to execute",
                         failure);
            }
        };

        BulkIngester.Builder<Void> builder = new BulkIngester.Builder<Void>().client(this.client)
                                                                             .maxOperations(this.settings.getFlushCount())
                                                                             .listener(listener);

        if (this.settings.getFlushInterval() != null) {
            builder.flushInterval(this.settings.getFlushInterval(), TimeUnit.MILLISECONDS);
        }

        if (this.settings.getFlushBytes() != null) {
            builder.maxSize(this.settings.getFlushBytes());
        }

        this.bulkIngester = builder.build();

        if (this.customProperties != null) {
            for (ECLAProperty property : this.customProperties.getProperties()) {
                property.start(getContext());
            }
        }

        if (this.settings.isCreateIndexTemplate()) {
            createIndexTemplate();
        }

        super.start();
    }

    @Override
    public void stop() {
        this.bulkIngester.close();

        try {
            this.client._transport().close();
        } catch (IOException e) {
            addError("An error occurred while closing elastic's client transport", e);
        }

        super.stop();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        try {
            CreateOperation<?> builder = new CreateOperation.Builder<>().index(this.settings.getDataStreamName())
                                                                        .pipeline(this.settings.getPipeline())
                                                                        .document(new ECLEvent(eventObject, this.settings, this.hostname, this.exPatternLayout,
                                                                                               this.customProperties))
                                                                        .build();
            this.bulkIngester.add(b -> b.create(builder));
        } catch (Exception e) {
            addError("An error occurred while preparing log for ECLA", e);
        }
    }

    private void createIndexTemplate() {
        ElasticsearchIlmClient ilmClient = new ElasticsearchIlmClient(this.client._transport());
        try {
            ilmClient.getLifecycle(b -> b.name(this.settings.getLifeCycleName()));
        } catch (ElasticsearchException e) {
            try {
                if (404 == e.response().status()) {
                    try (InputStream stream = ElasticClientLoggingAppender.class.getResourceAsStream(this.settings.getLifeCycleLocation())) {
                        PutLifecycleResponse response = ilmClient.putLifecycle(b -> b.withJson(stream).name(this.settings.getLifeCycleName()));

                        if (response.acknowledged()) {
                            addInfo("Created lifecycle for http trace");
                        } else {
                            addError("Could not create lifecycle " + this.settings.getLifeCycleName() + " for ECLA");
                        }
                    }

                } else {
                    addError("Could not create lifecycle " + this.settings.getLifeCycleName() + " for ECLA because: " + e.getMessage(), e);
                }
            } catch (Exception e1) {
                addError("Could not create lifecycle " + this.settings.getLifeCycleName() + " for ECLA because: " + e1.getMessage(), e1);
            }
        } catch (Exception e) {
            addError("Could not create lifecycle " + this.settings.getLifeCycleName() + " for ECLA because: " + e.getMessage(), e);
        }

        try {
            if (!this.client.indices().existsIndexTemplate(b -> b.name(this.settings.getIndexTemplateName())).get().value()) {
                try (InputStream stream = ElasticClientLoggingAppender.class.getResourceAsStream(this.settings.getIndexTemplateLocation())) {
                    CompletableFuture<PutIndexTemplateResponse> response = this.client.indices()
                                                                                      .putIndexTemplate(
                                                                                              b -> b.withJson(stream)
                                                                                                    .name(this.settings.getIndexTemplateName())
                                                                                                    .indexPatterns(this.settings.getDataStreamName() + "*"));

                    if (response.get().acknowledged()) {
                        addInfo("Created index template " + this.settings.getIndexTemplateName() + " for ECLA");
                    } else {
                        addError("Could not create index template " + this.settings.getIndexTemplateName() + " for ECLA");
                    }
                }
            }
        } catch (Exception e) {
            addError("Could not create index template " + this.settings.getIndexTemplateName() + " for ECLA because: " + e.getMessage(), e);
        }
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }

    public void setSettings(ECLASettings settings) {
        this.settings = settings;
    }

    public void setCustomProperties(ECLAProperties customProperties) {
        this.customProperties = customProperties;
    }
}
