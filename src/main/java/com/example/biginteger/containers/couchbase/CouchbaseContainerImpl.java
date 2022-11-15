package com.example.biginteger.containers.couchbase;

import com.example.biginteger.config.CouchbaseProperties;
import com.example.biginteger.containers.Container;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by mmonti on 2/3/21.
 */
@RequiredArgsConstructor
public class CouchbaseContainerImpl implements Container, DisposableBean, CouchbaseContainerConfiguration {

    private static final String IMAGE = "couchbase";
    private static final String VERSION = "latest";
    private static final String IMAGE_VERSION = IMAGE + ":" + VERSION;
    private static final String DEFAULT_UI_PORT = "8091";
    private static final String UI_PORT_BINDING = String.format("%s:%s", DEFAULT_UI_PORT, DEFAULT_UI_PORT);

    private static final AtomicBoolean STARTED = new AtomicBoolean(false);

    @Getter
    private static CouchbaseContainer couchbaseContainer;

    private final CouchbaseProperties couchbaseProperties;

    @Override
    public Container start() {
        if (!STARTED.get()) {
            final DockerImageName dockerImage = DockerImageName.parse(IMAGE_VERSION)
                    .asCompatibleSubstituteFor("couchbase/server");

            final String bucketName = couchbaseProperties.getBucketName();
            final String username = couchbaseProperties.getUsername();
            final String password = couchbaseProperties.getPassword();

            final BucketDefinition bucketDefinition = new BucketDefinition(bucketName)
                    .withPrimaryIndex(true);

            couchbaseContainer = new CouchbaseContainer(dockerImage)
                    .withBucket(bucketDefinition)
                    .withCredentials(username, password);
            couchbaseContainer.setPortBindings(List.of(UI_PORT_BINDING));
            couchbaseContainer.start();

            // We should not need this if we maintain the same bucket-name, username and pass.
            couchbaseProperties.setBucketName(bucketName);
            couchbaseProperties.setUsername(couchbaseContainer.getUsername());
            couchbaseProperties.setPassword(couchbaseContainer.getPassword());

            // The only property that needs to be changed is the connection string.
            couchbaseProperties.setConnectionString(couchbaseContainer.getConnectionString());

            STARTED.set(true);
        }
        return this;
    }

    @Override
    public String getConnectionString() {
        return couchbaseContainer.getConnectionString();
    }

    @Override
    public void stop() {
        if (couchbaseContainer != null) {
            couchbaseContainer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        return STARTED.get();
    }

    @Override
    public void destroy() throws Exception {
        this.stop();
    }
}
