package fr.graynaud.logbackelasticclientappender;

import co.elastic.clients.elasticsearch.ilm.PutLifecycleRequest;
import co.elastic.clients.elasticsearch.indices.PutIndexTemplateRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

public class ElasticClientLoggingAppenderTest {


    @Test
    public void should_not_throw_with_default_template() {
        ECLASettings settings = new ECLASettings();

        InputStream stream = ElasticClientLoggingAppender.class.getResourceAsStream(settings.getIndexTemplateLocation());

        Assertions.assertDoesNotThrow(() -> PutIndexTemplateRequest.of(b -> b.withJson(stream).name(settings.getIndexTemplateName())));
    }

    @Test
    public void should_not_throw_with_default_lifecycle() {
        ECLASettings settings = new ECLASettings();

        InputStream stream = ElasticClientLoggingAppender.class.getResourceAsStream(settings.getLifeCycleLocation());

        Assertions.assertDoesNotThrow(() -> PutLifecycleRequest.of(b -> b.withJson(stream).name(settings.getLifeCycleName())));
    }
}
