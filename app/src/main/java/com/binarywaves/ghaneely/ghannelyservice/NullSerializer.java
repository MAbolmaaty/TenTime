/**
 *
 */
package com.binarywaves.ghaneely.ghannelyservice;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author amostafa
 */
class NullSerializer extends JsonSerializer<Object> {
    public void serialize(Object value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        // any JSON value you want...
        jgen.writeString("");
    }
}
