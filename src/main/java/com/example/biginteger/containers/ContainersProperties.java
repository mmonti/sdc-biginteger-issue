package com.example.biginteger.containers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "big-integer-issue.containers")
public class ContainersProperties {

    private CouchbaseContainerProperties couchbase;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouchbaseContainerProperties {
        private boolean embeddedContainer;
        private boolean bootstrapData;
    }
}
