package org.osgi.service.converter;

import java.util.Collections;
import java.util.Map;


// Converts to-from string.
public interface Codec {
    default Codec configure(String key, Object val) {
        return configure(Collections.singletonMap(key, val));
    }

    Codec configure(Map<String, Object> m);

    Encoding encode(Object obj);
}
