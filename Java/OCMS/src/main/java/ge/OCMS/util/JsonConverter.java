package ge.OCMS.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JsonConverter {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private JsonConverter() {
    }

    public static <T> String toJson(T t) {
        if (t == null)
            return "null";
        try {
            return objectMapper.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            log.warn("Could not convert json");
        }
        return t.toString();
    }

    public static <T> T toEntity(String json, Class<T> c) {
        try {
            return objectMapper.readValue(json, c);
        } catch (JsonProcessingException e) {
            log.warn("Could not convert entity");
        }
        return null;
    }
}
