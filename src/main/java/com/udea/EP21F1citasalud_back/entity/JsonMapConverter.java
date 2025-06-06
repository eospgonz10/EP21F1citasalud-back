package com.udea.EP21F1citasalud_back.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class JsonMapConverter implements AttributeConverter<Map<String, Object>, Object> {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public Object convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) return null;
        try {
            String json = objectMapper.writeValueAsString(attribute);
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(json);
            return pgObject;
        } catch (JsonProcessingException | SQLException e) {
            throw new IllegalArgumentException("Error serializando JSON para jsonb", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(Object dbData) {
        if (dbData == null) return null;
        try {
            String json;
            if (dbData instanceof PGobject) {
                json = ((PGobject) dbData).getValue();
            } else {
                json = dbData.toString();
            }
            return objectMapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error deserializando JSON", e);
        }
    }
}
