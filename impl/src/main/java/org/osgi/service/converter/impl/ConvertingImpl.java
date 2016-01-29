package org.osgi.service.converter.impl;

import org.osgi.service.converter.Codec;
import org.osgi.service.converter.Converting;

public class ConvertingImpl implements Converting {
    private final Codec codec;
    private final Object object;

    ConvertingImpl(Codec c, Object obj) {
        codec = c;
        object = obj;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T to(Class<T> cls) {
        if (String.class.equals(cls)) {
            return (T) codec.encode(object);
        } else {
            return null;
        }
    }
}
