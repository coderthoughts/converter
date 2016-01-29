package org.osgi.service.converter.impl;

import java.util.Map;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.Encoding;

class EncodingImpl implements Encoding {
    private final Codec topCodec;
    private final Object object;

    EncodingImpl(Codec c, Object obj) {
        topCodec = c;
        object = obj;
    }

    @Override
    public String getString() {
        if (object == null)
            return null;

        if (object instanceof Map)
            return encodeMap((Map) object);

        return object.toString();
    }

    private String encodeMap(Map obj) {
        // TODO Auto-generated method stub
        return null;
    }
}
