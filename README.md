# LogbackElasticClientAppender

[![Release](https://img.shields.io/github/release/GaetanRAYNAUD/LogbackElasticClientAppender.svg)](https://github.com/zalando/logbook/releases)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

A library to send log to Elasticsearch directly from Logback. (No other server needed, your application send events
directly to your cluster)

Using the official [Elasticsearch Java API client](https://github.com/elastic/elasticsearch-java) sends logs
asynchronously to your Elasticsearch cluster.

Using a bulk ingester that will buffer events and flush them to the cluster based on interval, number of events or bytes
size of the buffer.

Supports basic authentication and api key based authentication.

Support custom properties with Logback layouts.

## How to use

Add the dependency to your project

Maven:

```xml

<dependency>
  <groupId>fr.graynaud</groupId>
  <artifactId>logback-elastic-client-appender</artifactId>
  <version>1.0.1</version>
</dependency>
```

Gradle:

```
implementation 'fr.graynaud:logback-elastic-client-appender:0.1.0'
```

Add the new appender to your Logback configuration

```xml

<appender name="ELASTIC" class="fr.graynaud.logbackelasticclientappender.ElasticClientLoggingAppenderTest">
  <url>https://your-elasticsearch-cluster</url>
  <dataStreamName>logs</dataStreamName>
  <createIndexTemplate>true</createIndexTemplate>
  <indexTemplateName>ecla-template</indexTemplateName>
  <lifeCycleName>ecla-lifecycle</lifeCycleName>
  <connectTimeout>30000</connectTimeout>
  <socketTimeout>30000</socketTimeout>
  <flushCount>1000</flushCount>
  <flushInterval>5000</flushInterval>
  <addMDC>true</addMDC>
  <exPattern>EX</exPattern>
  <tags>
    <tag>tag1</tag>
    <tag>tag2</tag>
  </tags>
  <!-- Choose one the following -->
  <authentication class="fr.graynaud.logbackelasticclientappender.authentication.BasicAuthentication">
    <username>${USERNAME}</username>
    <password>${PASSWORD}</password>
  </authentication>
  <authentication class="fr.graynaud.logbackelasticclientappender.authentication.ApiKeyAuthentication">
    <apiKey>${API_KEY}</apiKey>
  </authentication>
  <customProperties>
    <prop>
      <name>first_property</name>
      <value>static_value</value>
    </prop>
    <prop>
      <name>second_property</name>
      <value>${ENV_VAR}</value>
    </prop>
    <prop>
      <name>third_property</name>
      <value>%logback_var</value>
    </prop>
  </customProperties>
</appender>
```

## Requirements

- Java 11 or greater
- Elasticsearch version 8 or greater

## Settings

The list of available settings is as follows

| name                  | description                                                                                                                        | type                                                                                                                                                                                   | required | default value                                                                                         |
|-----------------------|------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------|-------------------------------------------------------------------------------------------------------|
| url                   | Url of elasticsearch cluster                                                                                                       | string                                                                                                                                                                                 | true     | none                                                                                                  |          
| dataStreamName        | Name of the data stream in which event are send                                                                                    | string                                                                                                                                                                                 | true     | logs                                                                                                  |          
| createIndexTemplate   | Define if an index template should be create at startup if does not already exists                                                 | boolean                                                                                                                                                                                | true     | false                                                                                                 |          
| indexTemplateName     | Name of the index template to create                                                                                               | string                                                                                                                                                                                 | false    | ecla-template                                                                                         |          
| indexTemplateLocation | Location of a json file with details of the body for the template creation request                                                 | string                                                                                                                                                                                 | false    | [template.json](src/main/resources/fr/graynaud/logbackelasticclientappender/template.json)            |          
| lifeCycleName         | Name of the life cycle to create associated to the template                                                                        | string                                                                                                                                                                                 | false    | ecla-lifecycle                                                                                        |          
| lifeCycleLocation     | Location of a json file with details of the body for the life cycle creation request                                               | string                                                                                                                                                                                 | false    | [lifecycle.json](src/main/resources/fr/graynaud/logbackelasticclientappender/lifecycle.json)          |          
| hostname              | Name of the machine send with every event                                                                                          | string                                                                                                                                                                                 | false    | Name of the machine base on OS environment variable<br/>Windows : COMPUTERNAME <br> Others : HOSTNAME |          
| pipeline              | Pipeline to execute when sending events                                                                                            | string                                                                                                                                                                                 | false    | none                                                                                                  |          
| connectTimeout        | Connection timeout in ms                                                                                                           | integer                                                                                                                                                                                | true     | 30000                                                                                                 |          
| socketTimeout         | Socket timeout in ms                                                                                                               | integer                                                                                                                                                                                | true     | 30000                                                                                                 |          
| flushCount            | Max size of the buffer before sending events to cluster (flush at what append first between flushCount, flushInterval, flushBytes) | integer                                                                                                                                                                                | true     | 1000                                                                                                  |          
| flushInterval         | Interval before flushing the buffer in ms (flush at what append first between flushCount, flushInterval, flushBytes)               | integer                                                                                                                                                                                | false    | none                                                                                                  |          
| flushBytes            | Max size in bytes of the buffer before flushing (flush at what append first between flushCount, flushInterval, flushBytes)         | long                                                                                                                                                                                   | false    | none                                                                                                  |          
| addMDC                | Add the MDC context to the event                                                                                                   | boolean                                                                                                                                                                                | true     | false                                                                                                 |          
| traceIdField          | Name of the trace id field in MDC mapped to trace.id in the event                                                                  | string                                                                                                                                                                                 | false    | none                                                                                                  |          
| spanIdField           | Name of the span id field in MDC mapped to span.id in the event                                                                    | string                                                                                                                                                                                 | false    | none                                                                                                  |          
| transactionIdField    | Name of the transaction id field in MDC mapped to transaction.id in the event                                                      | string                                                                                                                                                                                 | false    | none                                                                                                  |          
| exPattern             | Pattern of exception in the event                                                                                                  | [EX](https://logback.qos.ch/manual/layouts.html#ex) or [REX](https://logback.qos.ch/manual/layouts.html#rootException) or [XEX](https://logback.qos.ch/manual/layouts.html#xThrowable) | true     | EX                                                                                                    |          
| tags                  | List of tags to add to every event                                                                                                 | list of string                                                                                                                                                                         | true     | none                                                                                                  |
| authentication        | Type of authentication to connect to the cluster                                                                                   | Basic or ApiKey                                                                                                                                                                        | false    | none                                                                                                  |
