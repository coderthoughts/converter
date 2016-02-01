package org.osgi.service.converter.impl;

import java.util.Map;

import org.osgi.service.converter.Encoding;

class EncodingImpl implements Encoding {
    private final Object object;

    EncodingImpl(Object obj) {
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
