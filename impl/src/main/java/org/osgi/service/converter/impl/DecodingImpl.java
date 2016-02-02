package org.osgi.service.converter.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

import org.osgi.service.converter.Decoding;

public class DecodingImpl<T> implements Decoding<T> {
    private final Class<T> clazz;

    public DecodingImpl(Class<T> cls) {
        clazz = cls;
    }

    @Override
    public T from(CharSequence in) {
        if (Object[].class.isAssignableFrom(clazz)) {
            return createArray(in);
        }
        return tryStandardMethods(clazz, in);
    }

    private T createArray(CharSequence in) {
        Object[] res = (Object[]) Array.newInstance(clazz.getComponentType(), 1);
        res[0] = in;
        return (T) res;
    }

    @SuppressWarnings("unchecked")
    private <T> T tryStandardMethods(Class<T> cls, CharSequence cs) {
        try {
            Method m = cls.getDeclaredMethod("valueOf", String.class);
            if (m != null) {
                return (T) m.invoke(null, cs);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
