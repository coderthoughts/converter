package org.osgi.service.converter;

import java.util.Collections;
import java.util.Map;

// Converts to-from string.
public interface Codec {
    /**
     * Set a single codec configuration
     * @param key The configuration key
     * @param val The configuration value
     * @return The codec
     */
    default Codec configure(String key, Object val) {
        return configure(Collections.singletonMap(key, val));
    }

    /**
     * Configure this codec.
     * @param m Key-value pairs to configure the codec.
     * @return The codec
     */
    Codec configure(Map<String, Object> m);

    <T> Decoding<T> decode(Class<T> cls);

    /**
     * Encode the given object
     * @param obj The object to encode.
     * @return an Encoding object.
     */
    Encoding encode(Object obj);

    /**
     * Specify the top-level codec to use. This is used when
     * codec wrappers are in play to allow recursion into the
     * top-level codec.
     * @param topCodec The top-level codec to use.
     * @return The codec
     */
    Codec with(Converter converter);
}
