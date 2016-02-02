package org.osgi.service.converter.impl;

import java.lang.reflect.Method;

import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Converting;

public class ConvertingImpl implements Converting {
    private Converter converter;
    private final Object object;

    ConvertingImpl(Converter c, Object obj) {
        converter = c;
        object = obj;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T to(Class<T> cls) {
        if (String.class.equals(cls)) {
            if (object instanceof Object[])
                return (T) ((Object[])object)[0];
            return (T) object.toString();
        } else if (String[].class.equals(cls)) {
            String[] res = new String[1];
            res[0] = object.toString();
            return (T) res;
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

    public Converting with(Converter c) {
        converter = c;
        return this;
    }
}
