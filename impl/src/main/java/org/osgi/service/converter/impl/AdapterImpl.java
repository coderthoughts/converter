package org.osgi.service.converter.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.osgi.service.converter.Adapter;
import org.osgi.service.converter.Converter;
import org.osgi.service.converter.Converting;

public class AdapterImpl implements Adapter {
    private final Converter delegate;
    private final Map<Class<Object>, Function<Object, String>> toClassRules =
            new ConcurrentHashMap<>();
    private final Map<Class<Object>, Function<String, Object>> fromClassRules =
            new ConcurrentHashMap<>();

    public AdapterImpl(Converter converter) {
        this.delegate = converter;
    }

    @Override
    public Converting convert(Object obj) {
        Converting converting = delegate.convert(obj);
        return new ConvertingWrapper(obj, converting);
    }

    @Override
    public Adapter getAdapter() {
        return new AdapterImpl(this);
    }

    @Override
    public <T> Adapter rule(Class<T> cls, Function<T, String> toString, Function<String, T> fromString) {
        toClassRules.put((Class<Object>) cls, (Function<Object, String>) toString);
        fromClassRules.put((Class<Object>) cls, (Function<String, Object>) fromString);
        return this;
    }

    private class ConvertingWrapper implements Converting {
        private final Converting del;
        private final Object object;

        ConvertingWrapper(Object obj, Converting c) {
            object = obj;
            del = c;
        }

        @Override
        public <T> T to(Class<T> cls) {
            if (object instanceof String) {
                Function<String, Object> ff = fromClassRules.get(cls);
                if (ff != null)
                    return (T) ff.apply((String) object);
            }

            if (String.class.equals(cls)) {
                Function<Object, String> ft = toClassRules.get(object.getClass());
                if (ft != null)
                    return (T) ft.apply(object);
            }
            return del.to(cls);
        }
    }
}
