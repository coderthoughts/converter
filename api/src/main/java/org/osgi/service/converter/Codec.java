package org.osgi.service.converter;

import java.util.Collections;
import java.util.Map;

// Converts to-from string.
public interface Codec {
    default Codec configure(String key, Object val) {
        return configure(Collections.singletonMap(key, val));
    }

    Codec configure(Map<String, Object> m);

    default String encode(Object obj) {
        return encode(this, obj);
    }

    String encode(Codec top, Object obj);

    default <T> T decode(Class<T> cls) {
        return decode(this, cls);
    }

    <T> T decode(Codec top, Class<T> cls);
}
