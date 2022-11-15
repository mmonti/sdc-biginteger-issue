package com.example.biginteger.containers;

import com.example.biginteger.config.CouchbaseProperties;
import com.example.biginteger.containers.couchbase.CouchbaseContainerImpl;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(CouchbaseAutoConfiguration.class)
@EnableConfigurationProperties(value = { ContainersProperties.class })
public class ContainersConfig {

    @Bean(name = "couchbaseContainer")
    @ConditionalOnProperty(prefix = "big-integer-issue.containers.couchbase", name = "embedded-container", havingValue = "true")
    public Container couchbaseContainer(final CouchbaseProperties couchbaseProperties) {
        return new CouchbaseContainerImpl(couchbaseProperties).start();
    }

    @Bean(name = "couchbaseContainer")
    @ConditionalOnProperty(prefix = "big-integer-issue.containers.couchbase", name = "embedded-container", havingValue = "false")
    public Container noOpContainer() {
        return new Container.NoOpContainer();
    }

}