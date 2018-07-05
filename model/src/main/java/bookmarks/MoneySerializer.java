package bookmarks;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Criado por leonardo.segala em 04/07/2018.
 */
public class MoneySerializer extends JsonSerializer {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(((BigDecimal)o).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
    }
}
