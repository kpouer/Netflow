package com.kpouer.netflow.model.diameter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class DiameterDeserializer extends StdDeserializer<Diameter> {
    public DiameterDeserializer() {
        super(Diameter.class);
    }

    @Override
    public Diameter deserialize(JsonParser parser, DeserializationContext ctxt) throws IOException, JacksonException {
        var        diameter   = new Diameter();
        JsonNode node       = parser.getCodec().readTree(parser);
        diameter.setCode(node.get("diameter.cmd.code").asInt());

        return diameter;
    }
}
