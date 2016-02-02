package org.osgi.service.converter.impl;

import java.lang.reflect.Method;

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
            return (T) codec.encode(object).getString();
        } else if (object instanceof String) {
            return codec.decode(cls).from((String) object);
        }

        T res = tryStandardMethods(cls);

        if (res != null) {
            return res;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T tryStandardMethods(Class<T> cls) {
        try {
            Method m = cls.getDeclaredMethod("valueOf", String.class);
            if (m != null) {
                return (T) m.invoke(null, object);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public Converting with(Codec myCodec) {
        return new ConvertingImpl(myCodec, object);
    }
}
