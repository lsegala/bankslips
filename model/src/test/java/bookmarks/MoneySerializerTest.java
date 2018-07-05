package bookmarks;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.Assert.assertEquals;

/**
 * Criado por leonardo.segala em 05/07/2018.
 */
public class MoneySerializerTest {
    @Test
    public void mustConvertMoneyFormatProperly() throws IOException, ParseException {
        BigDecimal expectedResult = BigDecimal.valueOf(10000);
        Writer jsonWriter = new StringWriter();
        try(JsonGenerator jsonGenerator = new JsonFactory().createGenerator(jsonWriter)){
            SerializerProvider serializerProvider = new ObjectMapper().getSerializerProvider();
            new MoneySerializer().serialize(expectedResult, jsonGenerator, serializerProvider);
            jsonGenerator.flush();
            assertEquals("\""+expectedResult+"\"", jsonWriter.toString());
        }
    }
}
