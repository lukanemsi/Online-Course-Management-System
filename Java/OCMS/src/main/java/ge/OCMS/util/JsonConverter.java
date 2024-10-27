package ge.OCMS.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JsonConverter {

    private final static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

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

}
