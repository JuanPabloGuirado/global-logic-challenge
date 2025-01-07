package com.globallogic.backendchallenge.config;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson(JsonSerializer<LocalDateTime> localDateTimeAdapter) {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .create();
    }

    @Bean
    public JsonSerializer<LocalDateTime> localDateTimeAdapter() {
        return new LocalDateTimeAdapter();
    }

    /**
     *  Custom LocalDateTime adapter
     */
    public static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return new JsonPrimitive(src.format(formatter));
        }
    }
}