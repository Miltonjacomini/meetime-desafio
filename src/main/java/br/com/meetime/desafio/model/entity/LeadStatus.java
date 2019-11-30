package br.com.meetime.desafio.model.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.security.InvalidParameterException;
import java.util.stream.Stream;

@JsonDeserialize(using = LeadStatusDeserializer.class)
public enum LeadStatus {

    OPEN,
    WON,
    LOST;

    public static LeadStatus getByText(String status) {
        return Stream.of(values()).filter(st -> st.name().equalsIgnoreCase(status))
                     .findFirst()
                     .orElseThrow(() -> new InvalidParameterException("Status inválido"));
    }
}
