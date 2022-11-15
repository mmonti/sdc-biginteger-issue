package com.example.biginteger.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "big-integer-issue.couchbase")
public class CouchbaseProperties {

    private String connectionString;
    private String username;
    private String password;
    private String bucketName;

}
