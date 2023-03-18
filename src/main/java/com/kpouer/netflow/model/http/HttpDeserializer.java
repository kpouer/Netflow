package com.kpouer.netflow.model.http;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class HttpDeserializer extends StdDeserializer<Http> {

    public HttpDeserializer() {
        super(Http.class);
    }

    @Override
    public Http deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = parser.getCodec().readTree(parser);
        var firstField = node.fields().next();
        var value = firstField.getValue();
        var methodNode = value.get("http.request.method");
        var statusNode = value.get("http.response.code");
        var http = new Http();
        if (methodNode != null) {
            http.setMethod(methodNode.asText());
        }
        if (statusNode != null) {
            http.setStatus(statusNode.asInt());
        }
        return http;
    }
}
