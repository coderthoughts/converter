package org.osgi.service.converter.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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

        if (object instanceof Object[])
            return ((Object[]) object)[0].toString();

        if (object instanceof Map)
            return encodeMap((Map) object);

        return object.toString();
    }

    @Override
    public void to(OutputStream os) throws IOException {
        os.write(getString().getBytes(StandardCharsets.UTF_8));
    }

    private String encodeMap(Map obj) {
        // TODO Auto-generated method stub
        return null;
    }
}
