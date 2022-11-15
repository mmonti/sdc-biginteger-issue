package com.example.biginteger;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.kv.GetResult;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.openhft.compiler.CompilerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.math.BigInteger;
import java.util.Map;

@Slf4j
@SpringBootTest
public class BigIntegerIssue {

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;
    @Autowired
    private Cluster cluster;

    private Map map = Map.of("big", new BigInteger("12345678901234567890"));
    private Map nested = Map.of("parent", map);

    @BeforeEach
    public void setUp() {
        var savedMap = couchbaseTemplate.getCollection("_default").upsert("1", map);
        log.info("savedMap = {}", savedMap.mutationToken());
        var savedNestedMap = couchbaseTemplate.getCollection("_default").upsert("2", nested);
        log.info("savedNestedMap = {}", savedNestedMap.mutationToken());
    }

    @Test
    void test_store_and_retrieve_using_couchbase_driver_directly() {
        var bucket = cluster.bucket("bug");

        final GetResult resultMap = bucket.defaultCollection().get("1");
        final BigInteger bigInteger = (BigInteger) resultMap.contentAs(Map.class).get("big");
        log.info("Big integer value = {}", bigInteger);

        final GetResult resultNestedMap = bucket.defaultCollection().get("2");
        final Map<String, Map<String, Object>> nestedMap = (Map) resultNestedMap.contentAs(Map.class);
        final BigInteger bigIntegerOnNested = (BigInteger) nestedMap.get("parent").get("big");
        log.info("Big integer value = {}", bigIntegerOnNested);
    }

    @Test
    void test_fail_on_retrieve_saved_map_with_big_integer() {
        var retrievedMap = couchbaseTemplate.findById(Map.class).one("1");
        log.info("Big integer value = {}", retrievedMap.get("big"));
    }

    @Test
    void test_success_on_retrieve_saved_map_when_loading_custom_couchbase_simple_type() {
        // 1.- Before running, uncomment CouchbaseSimpleTypes.java in the test folder.

        var retrievedMap = couchbaseTemplate.findById(Map.class).one("1");
        log.info("Big integer value = {}", retrievedMap.get("big"));
    }

    @Test
    void test_fail_on_retrieve_nested_map_with_big_integer_key() {
        // 1.- Before running, uncomment CouchbaseSimpleTypes.java in the test folder.

        // Try to retrieve the nested map.
        var retrievedMap = couchbaseTemplate.findById(Map.class).one("2");
        log.info("Big integer value = {}", ((Map<String, Object>) retrievedMap.get("parent")).get("big"));
    }
}