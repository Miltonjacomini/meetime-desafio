package br.com.meetime.desafio.model.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;


public class LeadStatusDeserializer extends StdDeserializer<LeadStatus> {

    public LeadStatusDeserializer() {
        this(null);
    }

    public LeadStatusDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public LeadStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String name = node.asText();

        return LeadStatus.getByText(name);
    }
}