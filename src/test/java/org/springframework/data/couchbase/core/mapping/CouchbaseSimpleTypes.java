//package org.springframework.data.couchbase.core.mapping;
//
//import com.couchbase.client.java.json.JsonArray;
//import com.couchbase.client.java.json.JsonObject;
//
//import java.math.BigInteger;
//import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.springframework.data.mapping.model.SimpleTypeHolder;
//
//public abstract class CouchbaseSimpleTypes {
//    public static final SimpleTypeHolder JSON_TYPES = new SimpleTypeHolder((Set)Stream.of(JsonObject.class, JsonArray.class, Number.class).collect(Collectors.toSet()), true);
//    public static final SimpleTypeHolder DOCUMENT_TYPES = new SimpleTypeHolder((Set)Stream.of(BigInteger.class, CouchbaseDocument.class, CouchbaseList.class).collect(Collectors.toSet()), true);
//
//    private CouchbaseSimpleTypes() {
//    }
//}
