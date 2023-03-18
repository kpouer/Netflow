package com.kpouer.netflow;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpouer.netflow.model.*;
import com.kpouer.netflow.model.diameter.Diameter;
import com.kpouer.netflow.model.http.Http;
import com.kpouer.netflow.model.sip.Sip;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class Netflow {
    private static StarUmlPrinter printer;

    public static void main(String[] args) throws IOException {
        printer = new StarUmlPrinter();
        var start = System.currentTimeMillis();
        var file  = args[0];

        try (var is = new BufferedInputStream(Files.newInputStream(Paths.get(file)))) {
            var  uml        = parseStream(is);
            long afterParse = System.currentTimeMillis();
            System.out.println("Parsed in  " + (afterParse - start) + "ms");
            var reader = new SourceStringReader(uml);
            generateOutput("out.svg", reader, FileFormat.SVG);
            generateOutput("out.png", reader, FileFormat.PNG);
        }
    }

    private static void generateOutput(String file, SourceStringReader reader, FileFormat fileFormat) throws IOException {
        long start = System.currentTimeMillis();
        try (OutputStream os = Files.newOutputStream(Paths.get(file))) {
            reader.outputImage(os, new FileFormatOption(fileFormat));
            String desc = reader.outputImage(os).getDescription();
        }
        System.out.println("Generated " + file + " in " + (System.currentTimeMillis() - start) + "ms");
    }

    private static String parseStream(InputStream is) throws IOException {
        var objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try (var jsonParser = objectMapper.createParser(is)) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected content to be an array");
            }
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                var packet = parsePacket(jsonParser);
                packet.ifPresent(value -> printer.printPacket(value));
            }
        }
        System.out.println(printer.getUml());
        return printer.getUml();
    }

    private static Optional<Packet> parsePacket(JsonParser jsonParser) throws IOException {
        if (jsonParser.currentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected content to be an object but it is " + jsonParser.currentToken());
        }
        Packet packet = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            var property = jsonParser.getCurrentName();
            if ("_source".equals(property)) {
                jsonParser.nextToken();
                var sourceOptional = parseSource(jsonParser);
                if (sourceOptional.isPresent()) {
                    var source = sourceOptional.get();
                    packet = new Packet();
                    packet.set_source(source);
                }
            }
        }

        return Optional.ofNullable(packet);
    }

    private static Optional<Source> parseSource(JsonParser jsonParser) throws IOException {
        if (jsonParser.currentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected content to be an object");
        }
        Source source = null;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            var property = jsonParser.getCurrentName();
            if ("layers".equals(property)) {
                jsonParser.nextToken();
                var layerOptional = parseLayer(jsonParser);
                if (layerOptional.isPresent()) {
                    var layer = layerOptional.get();
                    source = new Source();
                    source.setLayers(layer);
                }
            }
        }

        return Optional.ofNullable(source);
    }

    private static Optional<Layers> parseLayer(JsonParser jsonParser) throws IOException {
        if (jsonParser.currentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected content to be an object");
        }
        var layers = new Layers();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            var property = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("ip".equals(property)) {
                layers.setIp(jsonParser.readValueAs(Ip.class));
            } else if ("http".equals(property)) {
                layers.setHttp(jsonParser.readValueAs(Http.class));
            } else if ("sip".equals(property)) {
                layers.setSip(jsonParser.readValueAs(Sip.class));
            } else if ("diameter".equals(property)) {
                var optional = parseDiameter(jsonParser);
                if (optional.isPresent()) {
                    var http = optional.get();
                    layers.setDiameter(http);
                }
            } else if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                jsonParser.skipChildren();
            }
        }

        if (layers.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(layers);
    }

    private static Optional<Diameter> parseDiameter(JsonParser jsonParser) throws IOException {
        if (jsonParser.currentToken() != JsonToken.START_OBJECT) {
            throw new IllegalStateException("Expected content to be an object");
        }
        var diameter = new Diameter();
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            var property = jsonParser.getCurrentName();
            jsonParser.nextToken();
            if ("diameter.cmd.code".equals(property)) {
                diameter.setCode(Integer.parseInt(jsonParser.getText()));
            } else if ("diameter.avp_tree".equals(property)) {
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    property = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("diameter.Result-Code".equals(property)) {
                        diameter.setResultCode(Integer.parseInt(jsonParser.getText()));
                    } else if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                        jsonParser.skipChildren();
                    }
                }
            } else if (jsonParser.currentToken() == JsonToken.START_OBJECT) {
                jsonParser.skipChildren();
            }
        }
        return Optional.of(diameter);
    }
}
